package com.prgrms.himin.menu.dto.response;

import com.prgrms.himin.menu.domain.MenuOption;

import lombok.Getter;

@Getter
public final class MenuOptionResponse {

	private final Long menuOptionId;

	private final String name;

	private final int price;

	private MenuOptionResponse(
		Long menuOptionId,
		String name,
		int price
	) {
		this.menuOptionId = menuOptionId;
		this.name = name;
		this.price = price;
	}

	public static MenuOptionResponse from(MenuOption menuOption) {
		return new MenuOptionResponse(
			menuOption.getId(),
			menuOption.getName(),
			menuOption.getPrice()
		);
	}
}
