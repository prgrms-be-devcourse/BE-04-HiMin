package com.prgrms.himin.setup.request;

import com.prgrms.himin.menu.dto.request.MenuUpdateRequest;

public class MenuUpdateRequestBuilder {
	public static MenuUpdateRequest.Info successBuild() {
		return new MenuUpdateRequest.Info(
			"짜장면",
			5000
		);
	}

	public static MenuUpdateRequest.Info failBuild(String input) {
		return new MenuUpdateRequest.Info(
			input,
			5000
		);
	}
}
