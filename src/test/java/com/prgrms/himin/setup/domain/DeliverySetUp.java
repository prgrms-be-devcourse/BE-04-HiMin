package com.prgrms.himin.setup.domain;

import org.springframework.stereotype.Component;

import com.prgrms.himin.delivery.domain.Delivery;
import com.prgrms.himin.delivery.domain.DeliveryHistory;
import com.prgrms.himin.delivery.domain.DeliveryHistoryRepository;
import com.prgrms.himin.delivery.domain.DeliveryRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class DeliverySetUp {

	private final DeliveryRepository deliveryRepository;

	private final DeliveryHistoryRepository deliveryHistoryRepository;

	public Delivery saveOne(Long orderId) {
		Delivery delivery = new Delivery(orderId);
		Delivery savedDelivery = deliveryRepository.save(delivery);

		DeliveryHistory deliveryHistory = DeliveryHistory.createBeforeDeliveryHistory(savedDelivery);
		deliveryHistoryRepository.save(deliveryHistory);

		return savedDelivery;
	}
}
