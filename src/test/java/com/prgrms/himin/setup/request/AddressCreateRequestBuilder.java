package com.prgrms.himin.setup.request;

import com.prgrms.himin.member.dto.request.AddressCreateRequest;

public class AddressCreateRequestBuilder {

	public static AddressCreateRequest successBuild() {
		return new AddressCreateRequest("추가우리집", "사근동 217-6");
	}

	public static AddressCreateRequest successBuild(String addressAlias, String address) {
		return new AddressCreateRequest(addressAlias, address);
	}
}
