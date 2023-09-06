package com.prgrms.himin.setup.request;

import java.util.List;

import com.prgrms.himin.order.dto.request.OrderCreateRequest;
import com.prgrms.himin.order.dto.request.SelectedMenuRequest;

public class OrderCreateRequestBuilder {

	public static OrderCreateRequest successBuild(
		Long memberId,
		Long shopId,
		List<SelectedMenuRequest> selectedMenus
	) {
		return new OrderCreateRequest(
			memberId,
			shopId,
			"성동구 사근동",
			"문앞에 두고 문자부탁드려요",
			selectedMenus
		);
	}

	public static OrderCreateRequest failBuild() {
		return new OrderCreateRequest(
			-1L,
			-1L,
			"성동구 사근동",
			"문앞에 두고 문자부탁드려요",
			null
		);
	}
}
