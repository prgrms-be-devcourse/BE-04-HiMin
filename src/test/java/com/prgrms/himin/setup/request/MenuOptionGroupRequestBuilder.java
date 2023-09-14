package com.prgrms.himin.setup.request;

import java.util.List;

import com.prgrms.himin.menu.dto.request.MenuOptionGroupCreateRequest;

public class MenuOptionGroupRequestBuilder {

	public static MenuOptionGroupCreateRequest successBuild() {
		return buildMenuOptionGroup("리뷰 이벤트 항목");
	}

	public static List<MenuOptionGroupCreateRequest> successesBuild() {
		MenuOptionGroupCreateRequest menuOptionGroupCreateRequest1
			= buildMenuOptionGroup("맵기 조절");

		MenuOptionGroupCreateRequest menuOptionGroupCreateRequest2
			= buildMenuOptionGroup("리뷰 이벤트 항목");

		MenuOptionGroupCreateRequest menuOptionGroupCreateRequest3
			= buildMenuOptionGroup("추가 메뉴");

		return List.of(
			menuOptionGroupCreateRequest1,
			menuOptionGroupCreateRequest2,
			menuOptionGroupCreateRequest3
		);
	}

	public static MenuOptionGroupCreateRequest failBuild(String name) {
		return buildMenuOptionGroup(name);
	}

	private static MenuOptionGroupCreateRequest buildMenuOptionGroup(String name) {
		return new MenuOptionGroupCreateRequest(name);
	}
}
