package com.prgrms.himin.setup.request;

import com.prgrms.himin.member.dto.request.AddressUpdateRequest;

public class AddressUpdateRequestBuilder {

	public static AddressUpdateRequest successBuild() {
		return new AddressUpdateRequest(
			"업데이트주소",
			"사근동217-217"
		);
	}
}
