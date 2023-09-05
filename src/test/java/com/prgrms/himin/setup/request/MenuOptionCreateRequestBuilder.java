package com.prgrms.himin.setup.request;

import com.prgrms.himin.menu.dto.request.MenuOptionCreateRequest;

public class MenuOptionCreateRequestBuilder {

	public static MenuOptionCreateRequest successBuild() {
		return new MenuOptionCreateRequest(
			"아주매운맛",
			1000
		);
	}

	public static MenuOptionCreateRequest failBuild(String name) {
		return new MenuOptionCreateRequest(
			name,
			5000
		);
	}
}
