package com.prgrms.himin.setup.request;

import java.util.List;

import com.prgrms.himin.menu.dto.request.MenuCreateRequest;

public class MenuCreateRequestBuilder {

	public static MenuCreateRequest successBuild() {
		return buildMenuCreateRequest("컵라면", 1000);
	}

	public static List<MenuCreateRequest> successesBuild() {
		final MenuCreateRequest menu1 = buildMenuCreateRequest("짜장면", 5000);
		final MenuCreateRequest menu2 = buildMenuCreateRequest("짬뽕", 6000);
		final MenuCreateRequest menu3 = buildMenuCreateRequest("탕수육", 15000);

		return List.of(
			menu1,
			menu2,
			menu3
		);
	}

	public static MenuCreateRequest failBuild(String name) {
		return buildMenuCreateRequest(name, 1000);
	}

	private static MenuCreateRequest buildMenuCreateRequest(
		String name,
		int price
	) {
		return new MenuCreateRequest(
			name,
			price,
			true
		);
	}
}
