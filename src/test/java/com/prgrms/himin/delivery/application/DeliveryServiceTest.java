package com.prgrms.himin.delivery.application;

import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.transaction.annotation.Transactional;

import com.prgrms.himin.delivery.domain.DeliveryHistory;
import com.prgrms.himin.delivery.domain.DeliveryHistoryRepository;
import com.prgrms.himin.delivery.domain.DeliveryRepository;
import com.prgrms.himin.delivery.domain.RiderRepository;
import com.prgrms.himin.delivery.dto.response.DeliveryResponse;

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
		}
	}
}