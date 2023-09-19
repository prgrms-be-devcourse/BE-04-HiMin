package com.prgrms.himin.setup.request;

import java.util.List;

import com.prgrms.himin.menu.dto.request.MenuOptionCreateRequest;

public class MenuOptionCreateRequestBuilder {

	public static MenuOptionCreateRequest successBuild() {
		return new MenuOptionCreateRequest("아주매운맛", 1000);
	}

	public static List<MenuOptionCreateRequest> successesBuild() {
		MenuOptionCreateRequest menuOption1 = buildMenuOption("매운맛", 3000);

		MenuOptionCreateRequest menuOption2 = buildMenuOption("순한맛", 10000);

		MenuOptionCreateRequest menuOption3 = buildMenuOption("수제 초콜릿", 3000);

		MenuOptionCreateRequest menuOption4 = buildMenuOption("수제 푸딩", 4000);

		MenuOptionCreateRequest menuOption5 = buildMenuOption("신라면", 12000);

		MenuOptionCreateRequest menuOption6 = buildMenuOption("진라면", 5000);

		return List.of(
			menuOption1,
			menuOption2,
			menuOption3,
			menuOption4,
			menuOption5,
			menuOption6
		);
	}

	private static MenuOptionCreateRequest buildMenuOption(
		String name,
		int price
	) {
		return new MenuOptionCreateRequest(name, price);
	}

	public static MenuOptionCreateRequest failBuild(String name) {
		return new MenuOptionCreateRequest(name, 5000);
	}
}
