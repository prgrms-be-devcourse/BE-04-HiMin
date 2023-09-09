package com.prgrms.himin.delivery.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.prgrms.himin.delivery.domain.Delivery;
import com.prgrms.himin.delivery.domain.DeliveryHistory;
import com.prgrms.himin.delivery.domain.DeliveryHistoryRepository;
import com.prgrms.himin.delivery.domain.DeliveryRepository;
import com.prgrms.himin.delivery.dto.response.DeliveryResponse;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class DeliveryService {

	private final DeliveryRepository deliveryRepository;

	private final DeliveryHistoryRepository deliveryHistoryRepository;

	@Transactional
	public DeliveryResponse createDelivery(Long orderId) {
		Delivery delivery = new Delivery(orderId);
		Delivery savedDelivery = deliveryRepository.save(delivery);

		DeliveryHistory deliveryHistory = DeliveryHistory.startedDeliveryHistory();
		DeliveryHistory savedDeliveryHistory = deliveryHistoryRepository.save(deliveryHistory);

		DeliveryResponse response = DeliveryResponse.from(
			savedDelivery,
			savedDeliveryHistory.getDeliveryStatus()
		);

		return response;
	}
}
