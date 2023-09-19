package com.prgrms.himin.setup.request;

import com.prgrms.himin.delivery.dto.request.RiderCreateRequest;

public class RiderCreateRequestBuilder {

	public static RiderCreateRequest successBuild() {
		return new RiderCreateRequest("박기사", "010-1234-5678");
	}
}
