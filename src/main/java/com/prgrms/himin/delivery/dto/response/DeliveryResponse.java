package com.prgrms.himin.delivery.dto.response;

import com.prgrms.himin.delivery.domain.Delivery;
import com.prgrms.himin.delivery.domain.DeliveryStatus;

public record DeliveryResponse(
	Long deliveryId,
	DeliveryStatus deliveryStatus
) {

	public static DeliveryResponse of(
		Delivery delivery,
		DeliveryStatus deliveryStatus
	) {
		return new DeliveryResponse(
			delivery.getDeliveryId(),
			deliveryStatus
		);
	}
}
