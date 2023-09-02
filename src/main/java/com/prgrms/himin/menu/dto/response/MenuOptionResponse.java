package com.prgrms.himin.menu.dto.response;

import com.prgrms.himin.menu.domain.MenuOption;

public record MenuOptionResponse(
	Long menuOptionId,
	String name,
	int price
) {

	public static MenuOptionResponse from(MenuOption menuOption) {
		return new MenuOptionResponse(
			menuOption.getId(),
			menuOption.getName(),
			menuOption.getPrice()
		);
	}
}
