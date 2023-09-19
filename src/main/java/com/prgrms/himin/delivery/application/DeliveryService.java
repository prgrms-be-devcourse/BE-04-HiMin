package com.prgrms.himin.delivery.application;

import java.util.List;

import org.springframework.context.ApplicationEventPublisher;
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
import com.prgrms.himin.global.error.exception.BusinessException;
import com.prgrms.himin.global.error.exception.EntityNotFoundException;
import com.prgrms.himin.global.error.exception.ErrorCode;
import com.prgrms.himin.order.domain.OrderValidator;
import com.prgrms.himin.order.event.DeliveryFinishedEvent;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class DeliveryService {

	private final DeliveryRepository deliveryRepository;

	private final DeliveryHistoryRepository deliveryHistoryRepository;

	private final RiderRepository riderRepository;

	private final ApplicationEventPublisher publisher;

	private final OrderValidator orderValidator;

	@Transactional
	public DeliveryResponse createDelivery(Long orderId) {
		orderValidator.validateOrderId(orderId);

		Delivery delivery = new Delivery(orderId);
		Delivery savedDelivery = deliveryRepository.save(delivery);

		DeliveryHistory deliveryHistory = DeliveryHistory.createBeforeDeliveryHistory(savedDelivery);
		DeliveryHistory savedDeliveryHistory = deliveryHistoryRepository.save(deliveryHistory);

		DeliveryResponse response = DeliveryResponse.from(savedDeliveryHistory);

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

		DeliveryHistory deliveryHistory = DeliveryHistory.createAllocatedDeliveryHistory(delivery);
		DeliveryHistory savedDeliveryHistory = deliveryHistoryRepository.save(deliveryHistory);

		DeliveryHistoryResponse response = DeliveryHistoryResponse.of(rider, savedDeliveryHistory);

		return response;
	}

	@Transactional
	public DeliveryHistoryResponse startDelivery(
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

		validateRider(riderId, delivery);

		DeliveryHistory deliveryHistory = DeliveryHistory.createStartDeliveryHistory(delivery);
		DeliveryHistory savedDeliveryHistory = deliveryHistoryRepository.save(deliveryHistory);

		DeliveryHistoryResponse response = DeliveryHistoryResponse.of(rider, savedDeliveryHistory);

		return response;
	}

	@Transactional
	public DeliveryHistoryResponse finishDelivery(
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

		validateRider(riderId, delivery);

		DeliveryHistory deliveryHistory = DeliveryHistory.createArrivedDeliveryHistory(delivery);
		DeliveryHistory savedDeliveryHistory = deliveryHistoryRepository.save(deliveryHistory);

		DeliveryHistoryResponse response = DeliveryHistoryResponse.of(rider, savedDeliveryHistory);

		publisher.publishEvent(new DeliveryFinishedEvent(delivery.getOrderId()));

		return response;
	}

	public DeliveryHistoryResponse.Multiple getDeliveryHistories(Long deliveryId) {
		Delivery delivery = deliveryRepository.findById(deliveryId)
			.orElseThrow(
				() -> new EntityNotFoundException(ErrorCode.DELIVERY_NOT_FOUND)
			);

		Rider rider = delivery.getRider();

		List<DeliveryHistory> deliveryHistories = deliveryHistoryRepository
			.findDeliveryHistoriesByDeliveryId(deliveryId);

		DeliveryHistoryResponse.Multiple responses = DeliveryHistoryResponse.Multiple.of(rider, deliveryHistories);

		return responses;
	}

	private void validateRider(
		Long riderId,
		Delivery delivery
	) {
		Rider rider = delivery.getRider();
		if (!riderId.equals(rider.getRiderId())) {
			throw new BusinessException(ErrorCode.DELIVERY_RIDER_BAD_REQUEST);
		}
	}
}
