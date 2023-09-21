package com.prgrms.himin.setup.response;

import java.time.LocalDateTime;
import java.util.List;

import com.prgrms.himin.delivery.domain.DeliveryStatus;
import com.prgrms.himin.delivery.dto.response.DeliveryHistoryResponse;

public class DeliveryHistoryResponseBuilder {

	public static DeliveryHistoryResponse allocateRiderHistoryBuild() {
		return new DeliveryHistoryResponse(
			1L,
			DeliveryHistoryResponse.createHistoryInfo(DeliveryStatus.ALLOCATED, LocalDateTime.now())
		);
	}

	public static DeliveryHistoryResponse startDeliveryHistoryBuild() {
		return new DeliveryHistoryResponse(
			1L,
			DeliveryHistoryResponse.createHistoryInfo(DeliveryStatus.DELIVERING, LocalDateTime.now())
		);
	}

	public static DeliveryHistoryResponse finishDeliveryHistoryBuild() {
		return new DeliveryHistoryResponse(
			1L,
			DeliveryHistoryResponse.createHistoryInfo(DeliveryStatus.ARRIVED, LocalDateTime.now())
		);
	}

	public static DeliveryHistoryResponse.Multiple multipleDeliveryHistoryBuild() {

		return new DeliveryHistoryResponse.Multiple(
			1L,
			List.of(
				DeliveryHistoryResponse.createHistoryInfo(DeliveryStatus.BEFORE_DELIVERY, LocalDateTime.now()),
				DeliveryHistoryResponse.createHistoryInfo(DeliveryStatus.ALLOCATED, LocalDateTime.now()),
				DeliveryHistoryResponse.createHistoryInfo(DeliveryStatus.DELIVERING, LocalDateTime.now()),
				DeliveryHistoryResponse.createHistoryInfo(DeliveryStatus.ARRIVED, LocalDateTime.now())
			)
		);
	}
}
