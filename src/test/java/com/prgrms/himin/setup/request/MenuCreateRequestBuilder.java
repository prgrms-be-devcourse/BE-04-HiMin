package com.prgrms.himin.setup.request;

import com.prgrms.himin.menu.dto.request.MenuCreateRequest;

public class MenuCreateRequestBuilder {

	public static MenuCreateRequest successBuild() {
		return new MenuCreateRequest(
			"짜장면",
			5000,
			true
		);
	}

	public static MenuCreateRequest failBuild(String name) {
		return new MenuCreateRequest(
			name,
			5000,
			true
		);
	}
}
