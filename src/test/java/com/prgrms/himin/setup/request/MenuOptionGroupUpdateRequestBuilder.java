package com.prgrms.himin.setup.request;

import com.prgrms.himin.menu.dto.request.MenuOptionGroupUpdateRequest;

public class MenuOptionGroupUpdateRequestBuilder {

	public static MenuOptionGroupUpdateRequest successBuild() {
		return new MenuOptionGroupUpdateRequest("주방장 선택");
	}

	public static MenuOptionGroupUpdateRequest failBuild(String name) {
		return new MenuOptionGroupUpdateRequest(name);
	}
}
