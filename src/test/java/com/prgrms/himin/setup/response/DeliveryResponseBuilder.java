package com.prgrms.himin.setup.response;

import com.prgrms.himin.delivery.domain.DeliveryStatus;
import com.prgrms.himin.delivery.dto.response.DeliveryResponse;

public class DeliveryResponseBuilder {

	public static DeliveryResponse beforeDeliveryBuild() {
		return new DeliveryResponse(
			1L,
			DeliveryStatus.BEFORE_DELIVERY
		);
	}
}
