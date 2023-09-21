package com.prgrms.himin.setup.response;

import java.util.List;

import com.prgrms.himin.member.dto.request.AddressCreateRequest;
import com.prgrms.himin.member.dto.response.AddressResponse;
import com.prgrms.himin.setup.request.AddressCreateRequestBuilder;

public class AddressResponseBuilder {

	public static AddressResponse successBuild() {
		String address = AddressCreateRequestBuilder.successBuild().address();
		String addressAlias = AddressCreateRequestBuilder.successBuild().addressAlias();

		return new AddressResponse(
			2L,
			address,
			addressAlias
		);
	}

	public static List<AddressResponse> successListBuild() {
		AddressCreateRequest request1 = AddressCreateRequestBuilder
			.successBuild("우리집1", "사근동217-5");

		AddressCreateRequest request2 = AddressCreateRequestBuilder
			.successBuild("우리집2", "사근동217-6");

		return List.of(
			new AddressResponse(
				1L,
				request1.address(),
				request1.addressAlias()
			),
			new AddressResponse(
				2L,
				request2.address(),
				request2.addressAlias()
			)
		);
	}
}
