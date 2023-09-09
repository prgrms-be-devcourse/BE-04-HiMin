package com.prgrms.himin.delivery.dto.response;

import com.prgrms.himin.delivery.domain.Delivery;
import com.prgrms.himin.delivery.domain.DeliveryHistory;
import com.prgrms.himin.delivery.domain.DeliveryStatus;

public record DeliveryResponse(
	Long deliveryId,
	DeliveryStatus deliveryStatus
) {

	public static DeliveryResponse of(
		Delivery delivery,
		DeliveryHistory deliveryHistory
	) {
		return new DeliveryResponse(
			delivery.getDeliveryId(),
			deliveryHistory.getDeliveryStatus()
		);
	}
}
