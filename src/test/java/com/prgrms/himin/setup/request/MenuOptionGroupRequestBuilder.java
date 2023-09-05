package com.prgrms.himin.setup.request;

import com.prgrms.himin.menu.dto.request.MenuOptionGroupCreateRequest;

public class MenuOptionGroupRequestBuilder {

	public static MenuOptionGroupCreateRequest successBuild() {
		return new MenuOptionGroupCreateRequest(
			"리뷰 이벤트 항목"
		);
	}

	public static MenuOptionGroupCreateRequest failBuild(String name) {
		return new MenuOptionGroupCreateRequest(
			name
		);
	}
}
