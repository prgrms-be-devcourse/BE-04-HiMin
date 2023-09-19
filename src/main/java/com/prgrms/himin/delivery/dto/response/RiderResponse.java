package com.prgrms.himin.delivery.dto.response;

import com.prgrms.himin.delivery.domain.Rider;

public record RiderResponse(
	Long riderId,
	String name,
	String phone
) {

	public static RiderResponse from(Rider rider) {
		return new RiderResponse(rider.getRiderId(), rider.getName(), rider.getPhone());
	}
}
