package com.prgrms.himin.delivery.dto.response;

import java.time.LocalDateTime;

import com.prgrms.himin.delivery.domain.DeliveryHistory;
import com.prgrms.himin.delivery.domain.DeliveryStatus;
import com.prgrms.himin.delivery.domain.Rider;

public record DeliveryHistoryResponse(
	Long riderId,
	DeliveryStatus deliveryStatus,
	LocalDateTime createdAt
) {

	public static DeliveryHistoryResponse of(
		Rider rider,
		DeliveryHistory deliveryHistory
	) {
		return new DeliveryHistoryResponse(
			rider.getRiderId(),
			deliveryHistory.getDeliveryStatus(),
			deliveryHistory.getCreatedAt()
		);
	}
}
