package com.prgrms.himin.delivery.dto.response;

import com.prgrms.himin.delivery.domain.DeliveryHistory;
import com.prgrms.himin.delivery.domain.DeliveryStatus;

public record DeliveryResponse(
	Long deliveryId,
	DeliveryStatus deliveryStatus
) {

	public static DeliveryResponse from(DeliveryHistory deliveryHistory) {
		return new DeliveryResponse(
			deliveryHistory.getDelivery().getDeliveryId(),
			deliveryHistory.getDeliveryStatus()
		);
	}
}
