package com.prgrms.himin.menu.dto.response;

import com.prgrms.himin.menu.domain.MenuOption;

public record MenuOptionCreateResponse(
	Long menuOptionId,
	String name,
	int price
) {

	public static MenuOptionCreateResponse from(MenuOption menuOption) {
		return new MenuOptionCreateResponse(
			menuOption.getId(),
			menuOption.getName(),
			menuOption.getPrice()
		);
	}
}
