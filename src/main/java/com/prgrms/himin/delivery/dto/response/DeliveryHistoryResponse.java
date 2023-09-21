package com.prgrms.himin.delivery.dto.response;

import java.time.LocalDateTime;
import java.util.List;

import com.prgrms.himin.delivery.domain.DeliveryHistory;
import com.prgrms.himin.delivery.domain.DeliveryStatus;
import com.prgrms.himin.delivery.domain.Rider;

public record DeliveryHistoryResponse(
	Long riderId,
	HistoryInfo historyInfo
) {

	public static DeliveryHistoryResponse of(
		Rider rider,
		DeliveryHistory deliveryHistory
	) {
		HistoryInfo historyInfo = new HistoryInfo(deliveryHistory.getDeliveryStatus(), deliveryHistory.getCreatedAt());

		return new DeliveryHistoryResponse(rider.getRiderId(), historyInfo);
	}

	public record Multiple(
		Long riderId,
		List<HistoryInfo> historyInfos
	) {

		public static DeliveryHistoryResponse.Multiple of(
			Rider rider,
			List<DeliveryHistory> deliveryHistories
		) {
			List<HistoryInfo> historyInfos = deliveryHistories.stream()
				.map(deliveryHistory -> new HistoryInfo(
					deliveryHistory.getDeliveryStatus(),
					deliveryHistory.getCreatedAt()
				))
				.toList();

			return new DeliveryHistoryResponse.Multiple(
				rider.getRiderId(),
				historyInfos
			);
		}
	}

	private record HistoryInfo(
		DeliveryStatus deliveryStatus,
		LocalDateTime createdAt
	) {
	}

	public static HistoryInfo createHistoryInfo(
		DeliveryStatus deliveryStatus,
		LocalDateTime createdAt
	) {
		return new HistoryInfo(deliveryStatus, createdAt);
	}

	public DeliveryStatus deliveryStatus() {
		return this.historyInfo.deliveryStatus();
	}
}
