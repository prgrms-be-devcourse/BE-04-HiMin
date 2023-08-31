package com.prgrms.himin.menu.dto.response;

import com.prgrms.himin.menu.domain.MenuOption;

import lombok.Getter;

@Getter
public final class MenuOptionCreateResponse {

	private final Long menuOptionId;

	private final String name;

	private final int price;

	private MenuOptionCreateResponse(
		Long menuOptionId,
		String name,
		int price
	) {
		this.menuOptionId = menuOptionId;
		this.name = name;
		this.price = price;
	}

	public static MenuOptionCreateResponse from(MenuOption menuOption) {
		return new MenuOptionCreateResponse(
			menuOption.getId(),
			menuOption.getName(),
			menuOption.getPrice()
		);
	}
}
