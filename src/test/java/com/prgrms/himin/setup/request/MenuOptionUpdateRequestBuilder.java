package com.prgrms.himin.setup.request;

import com.prgrms.himin.menu.dto.request.MenuOptionUpdateRequest;

public class MenuOptionUpdateRequestBuilder {

	public static MenuOptionUpdateRequest successBuild() {
		return new MenuOptionUpdateRequest("가지맛", 500);
	}

	public static MenuOptionUpdateRequest failBuild(String name) {
		return new MenuOptionUpdateRequest(name, 5000);
	}
}
