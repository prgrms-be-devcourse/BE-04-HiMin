package com.prgrms.himin.delivery.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.prgrms.himin.delivery.domain.Delivery;
import com.prgrms.himin.delivery.domain.DeliveryHistory;
import com.prgrms.himin.delivery.domain.DeliveryHistoryRepository;
import com.prgrms.himin.delivery.domain.DeliveryRepository;
import com.prgrms.himin.delivery.domain.Rider;
import com.prgrms.himin.delivery.domain.RiderRepository;
import com.prgrms.himin.delivery.dto.response.DeliveryHistoryResponse;
import com.prgrms.himin.delivery.dto.response.DeliveryResponse;
import com.prgrms.himin.global.error.exception.EntityNotFoundException;
import com.prgrms.himin.global.error.exception.ErrorCode;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class DeliveryService {

	private final DeliveryRepository deliveryRepository;

	private final DeliveryHistoryRepository deliveryHistoryRepository;

	private final RiderRepository riderRepository;

	@Transactional
	public DeliveryResponse createDelivery(Long orderId) {
		Delivery delivery = new Delivery(orderId);
		Delivery savedDelivery = deliveryRepository.save(delivery);

		DeliveryHistory deliveryHistory = DeliveryHistory.createdDeliveryHistory(savedDelivery);
		DeliveryHistory savedDeliveryHistory = deliveryHistoryRepository.save(deliveryHistory);

		DeliveryResponse response = DeliveryResponse.of(
			savedDelivery,
			savedDeliveryHistory
		);

		return response;
	}

	@Transactional
	public DeliveryHistoryResponse allocateRider(
		Long deliveryId,
		Long riderId
	) {
		Delivery delivery = deliveryRepository.findById(deliveryId)
			.orElseThrow(
				() -> new EntityNotFoundException(ErrorCode.DELIVERY_NOT_FOUND)
			);

		Rider rider = riderRepository.findById(riderId)
			.orElseThrow(
				() -> new EntityNotFoundException(ErrorCode.DELIVERY_NOT_FOUND)
			);

		delivery.attach(rider);

		DeliveryHistory deliveryHistory = DeliveryHistory.allocatedDeliveryHistory(delivery);
		DeliveryHistory savedDeliveryHistory = deliveryHistoryRepository.save(deliveryHistory);

		DeliveryHistoryResponse response = DeliveryHistoryResponse.of(
			rider,
			savedDeliveryHistory
		);

		return response;
	}
}
