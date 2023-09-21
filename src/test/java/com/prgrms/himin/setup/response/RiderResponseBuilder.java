package com.prgrms.himin.setup.response;

import com.prgrms.himin.delivery.dto.response.RiderResponse;

public class RiderResponseBuilder {

	public static RiderResponse successBuild() {
		return new RiderResponse(
			1L,
			"박기사",
			"010-1234-5678"
		);
	}
}
