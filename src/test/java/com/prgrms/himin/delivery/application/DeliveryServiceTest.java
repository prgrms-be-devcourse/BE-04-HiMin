package com.prgrms.himin.delivery.application;

import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.transaction.annotation.Transactional;

import com.prgrms.himin.delivery.domain.Delivery;
import com.prgrms.himin.delivery.domain.DeliveryHistory;
import com.prgrms.himin.delivery.domain.DeliveryHistoryRepository;
import com.prgrms.himin.delivery.domain.DeliveryRepository;
import com.prgrms.himin.delivery.domain.DeliveryStatus;
import com.prgrms.himin.delivery.domain.Rider;
import com.prgrms.himin.delivery.domain.RiderRepository;
import com.prgrms.himin.delivery.dto.response.DeliveryHistoryResponse;
import com.prgrms.himin.delivery.dto.response.DeliveryResponse;
import com.prgrms.himin.global.error.exception.BusinessException;
import com.prgrms.himin.global.error.exception.EntityNotFoundException;
import com.prgrms.himin.setup.domain.DeliverySetUp;
import com.prgrms.himin.setup.domain.RiderSetUp;

@Transactional
@SpringBootTest
class DeliveryServiceTest {

	@Autowired
	DeliveryService deliveryService;

	@Autowired
	DeliveryRepository deliveryRepository;

	@Autowired
	DeliveryHistoryRepository deliveryHistoryRepository;

	@Autowired
	RiderRepository riderRepository;

	@Autowired
	ApplicationEventPublisher publisher;

	@Autowired
	DeliverySetUp deliverySetUp;

	@Autowired
	RiderSetUp riderSetUp;

	@Nested
	@DisplayName("배달을 생성할 수 있다.")
	class createDelivery {

		@Test
		@DisplayName("성공한다.")
		void success_test() {
			// when
			DeliveryResponse deliveryResponse = deliveryService.createDelivery(1L); // orderId값에 아무 값이나 넣어도 통과됨

			// then
			Long deliveryId = deliveryResponse.deliveryId();
			List<DeliveryHistory> deliveryHistories = deliveryHistoryRepository.findDeliveryHistoriesByDeliveryId(
				deliveryId);
			boolean result = deliveryRepository.existsById(deliveryId);
			assertThat(deliveryHistories).hasSize(1);
			assertThat(result).isTrue();
			assertThat(deliveryResponse.deliveryStatus()).isEqualTo(DeliveryStatus.BEFORE_DELIVERY);
		}
	}

	@Nested
	@DisplayName("배달기사를 배정할 수 있다.")
	class allocateRider {

		@Test
		@DisplayName("성공한다.")
		void success_test() {
			// given
			Delivery delivery = deliverySetUp.saveOne(1L);
			Rider rider = riderSetUp.saveOne();

			// when
			DeliveryHistoryResponse deliveryHistoryResponse = deliveryService.allocateRider(delivery.getDeliveryId(),
				rider.getRiderId());

			// then
			List<DeliveryHistory> deliveryHistories = deliveryHistoryRepository.findDeliveryHistoriesByDeliveryId(
				delivery.getDeliveryId());
			assertThat(deliveryHistories).hasSize(2);
			assertThat(delivery.getRider().getRiderId()).isEqualTo(rider.getRiderId());
			assertThat(deliveryHistoryResponse.historyInfo().deliveryStatus()).isEqualTo(DeliveryStatus.ALLOCATED);
		}

		@Test
		@DisplayName("배달이 존재하지 않아서 실패한다.")
		void not_exist_delivery_fail_test() {
			// given
			Rider rider = riderSetUp.saveOne();

			Long wrongId = 0L;

			// when & then
			assertThatThrownBy(
				() -> deliveryService.allocateRider(wrongId, rider.getRiderId())
			)
				.isInstanceOf(EntityNotFoundException.class);
		}

		@Test
		@DisplayName("배달기사가 존재하지 않아서 실패한다.")
		void not_exist_rider_fail_test() {
			// given
			Delivery delivery = deliverySetUp.saveOne(1L);

			Long wrongId = 0L;

			// when & then
			assertThatThrownBy(
				() -> deliveryService.allocateRider(delivery.getDeliveryId(), wrongId)
			)
				.isInstanceOf(EntityNotFoundException.class);
		}
	}

	@Nested
	@DisplayName("배달을 시작할 수 있다.")
	class startDelivery {

		Delivery delivery;
		Rider rider;

		@BeforeEach
		void allocateRider() {
			delivery = deliverySetUp.saveOne(1L);
			rider = riderSetUp.saveOne();

			deliveryService.allocateRider(delivery.getDeliveryId(), rider.getRiderId());
		}

		@Test
		@DisplayName("성공한다.")
		void success_test() {
			// when
			DeliveryHistoryResponse deliveryHistoryResponse = deliveryService.startDelivery(delivery.getDeliveryId(),
				rider.getRiderId());

			// then
			List<DeliveryHistory> deliveryHistories = deliveryHistoryRepository.findDeliveryHistoriesByDeliveryId(
				delivery.getDeliveryId());
			assertThat(deliveryHistories).hasSize(3);
			assertThat(deliveryHistoryResponse.historyInfo().deliveryStatus()).isEqualTo(DeliveryStatus.DELIVERING);
		}

		@Test
		@DisplayName("배달이 존재하지 않아서 실패한다.")
		void not_exist_delivery_fail_test() {
			// given
			Long wrongId = 0L;

			// when & then
			assertThatThrownBy(
				() -> deliveryService.startDelivery(wrongId, rider.getRiderId())
			)
				.isInstanceOf(EntityNotFoundException.class);
		}

		@Test
		@DisplayName("배달기사가 존재하지 않아서 실패한다.")
		void not_exist_rider_fail_test() {
			// given
			Long wrongId = 0L;

			// when & then
			assertThatThrownBy(
				() -> deliveryService.startDelivery(delivery.getDeliveryId(), wrongId)
			)
				.isInstanceOf(EntityNotFoundException.class);
		}

		@Test
		@DisplayName("배달과 배달기사가 맞지 않아서 실패한다.")
		void not_match_delivery_and_rider_fail_test() {
			// given
			Rider anotherRider = riderSetUp.saveOne();

			// when & then
			assertThatThrownBy(
				() -> deliveryService.startDelivery(delivery.getDeliveryId(), anotherRider.getRiderId())
			)
				.isInstanceOf(BusinessException.class);
		}
	}
}