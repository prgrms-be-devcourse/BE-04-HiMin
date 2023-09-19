package com.prgrms.himin.delivery.application;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.test.context.jdbc.Sql;

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
import com.prgrms.himin.order.application.OrderService;
import com.prgrms.himin.order.domain.OrderValidator;
import com.prgrms.himin.setup.domain.DeliverySetUp;
import com.prgrms.himin.setup.domain.RiderSetUp;

@SpringBootTest
@Sql("/truncate.sql")
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

	Delivery delivery;

	Rider rider;

	@SpyBean
	OrderValidator orderValidator;

	@SpyBean
	OrderService orderService;

	@BeforeEach
	void setUp() {
		delivery = deliverySetUp.saveOne(1L);
		rider = riderSetUp.saveOne();
	}

	@Nested
	@DisplayName("배달을 생성할 수 있다.")
	class createDelivery {

		@Test
		@DisplayName("성공한다.")
		void success_test() {
			// given
			doNothing().when(orderValidator).validateOrderId(anyLong());

			// when
			DeliveryResponse deliveryResponse = deliveryService.createDelivery(0L);

			// then
			Long deliveryId = deliveryResponse.deliveryId();
			List<DeliveryHistory> deliveryHistories = deliveryHistoryRepository
				.findDeliveryHistoriesByDeliveryId(deliveryId);

			boolean result = deliveryRepository.existsById(deliveryId);
			assertThat(deliveryHistories).hasSize(1);
			assertThat(result).isTrue();
			assertThat(deliveryResponse.deliveryStatus()).isEqualTo(DeliveryStatus.BEFORE_DELIVERY);
		}

		@DisplayName("주문id가 존재하지 않아 실패한다.")
		@Test
		void not_exist_order_id_fail_test() {
			// given
			Long wrongId = 0L;

			// when & then
			assertThatThrownBy(
				() -> deliveryService.createDelivery(wrongId)
			)
				.isInstanceOf(EntityNotFoundException.class);
		}
	}

	@Nested
	@DisplayName("배달기사를 배정할 수 있다.")
	class allocateRider {

		@Test
		@DisplayName("성공한다.")
		void success_test() {
			// when
			DeliveryHistoryResponse deliveryHistoryResponse = deliveryService
				.allocateRider(delivery.getDeliveryId(), rider.getRiderId());

			// then
			Delivery savedDelivery = deliveryRepository.findById(delivery.getDeliveryId()).get();
			List<DeliveryHistory> deliveryHistories = deliveryHistoryRepository
				.findDeliveryHistoriesByDeliveryId(delivery.getDeliveryId());
			assertThat(deliveryHistories).hasSize(2);
			assertThat(savedDelivery.getRider().getRiderId()).isEqualTo(rider.getRiderId());
			assertThat(deliveryHistoryResponse.deliveryStatus()).isEqualTo(DeliveryStatus.ALLOCATED);
		}

		@Test
		@DisplayName("배달이 존재하지 않아서 실패한다.")
		void not_exist_delivery_fail_test() {
			// given
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

		@BeforeEach
		void allocateRider() {
			deliveryService.allocateRider(delivery.getDeliveryId(), rider.getRiderId());
		}

		@Test
		@DisplayName("성공한다.")
		void success_test() {
			// when
			DeliveryHistoryResponse deliveryHistoryResponse = deliveryService
				.startDelivery(delivery.getDeliveryId(), rider.getRiderId());

			// then
			List<DeliveryHistory> deliveryHistories = deliveryHistoryRepository
				.findDeliveryHistoriesByDeliveryId(delivery.getDeliveryId());
			assertThat(deliveryHistories).hasSize(3);
			assertThat(deliveryHistoryResponse.deliveryStatus()).isEqualTo(DeliveryStatus.DELIVERING);
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

	@Nested
	@DisplayName("배달을 완료할 수 있다.")
	class finishDelivery {

		@BeforeEach
		void setDeliveryHistory() {
			deliveryService.allocateRider(delivery.getDeliveryId(), rider.getRiderId());
			deliveryService.startDelivery(delivery.getDeliveryId(), rider.getRiderId());
		}

		@Test
		@DisplayName("성공한다.")
		void success_test() {
			// given
			doNothing().when(orderService).finishOrder(anyLong());

			// when
			DeliveryHistoryResponse deliveryHistoryResponse = deliveryService
				.finishDelivery(delivery.getDeliveryId(), rider.getRiderId());

			// then
			List<DeliveryHistory> deliveryHistories = deliveryHistoryRepository
				.findDeliveryHistoriesByDeliveryId(delivery.getDeliveryId());
			assertThat(deliveryHistories).hasSize(4);
			assertThat(deliveryHistoryResponse.deliveryStatus()).isEqualTo(DeliveryStatus.ARRIVED);
		}

		@Test
		@DisplayName("배달이 존재하지 않아서 실패한다.")
		void not_exist_delivery_fail_test() {
			// given
			Long wrongId = 0L;

			// when & then
			assertThatThrownBy(
				() -> deliveryService.finishDelivery(wrongId, rider.getRiderId())
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
				() -> deliveryService.finishDelivery(delivery.getDeliveryId(), wrongId)
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
				() -> deliveryService.finishDelivery(delivery.getDeliveryId(), anotherRider.getRiderId())
			)
				.isInstanceOf(BusinessException.class);
		}
	}

	@Nested
	@DisplayName("배달 내역을 조회할 수 있다.")
	class getDeliveryHistories {

		@BeforeEach
		void setDeliveryHistory() {
			doNothing().when(orderService).finishOrder(anyLong());
			deliveryService.allocateRider(delivery.getDeliveryId(), rider.getRiderId());
			deliveryService.startDelivery(delivery.getDeliveryId(), rider.getRiderId());
			deliveryService.finishDelivery(delivery.getDeliveryId(), rider.getRiderId());
		}

		@Test
		@DisplayName("성공한다.")
		void success_test() {
			// when
			DeliveryHistoryResponse.Multiple deliveryHistories = deliveryService
				.getDeliveryHistories(delivery.getDeliveryId());

			// then
			assertThat(deliveryHistories.historyInfos()).hasSize(4);
		}

		@Test
		@DisplayName("배달이 존재하지 않아서 실패한다.")
		void not_exist_delivery_fail_test() {
			// given
			Long wrongId = 0L;

			// when & then
			assertThatThrownBy(
				() -> deliveryService.getDeliveryHistories(wrongId)
			)
				.isInstanceOf(EntityNotFoundException.class);
		}
	}
}
