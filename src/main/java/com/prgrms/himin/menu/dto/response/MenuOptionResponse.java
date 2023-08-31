package com.prgrms.himin.menu.dto.response;

import com.prgrms.himin.menu.domain.MenuOption;

import lombok.Getter;

@Getter
public final class MenuOptionResponse {

	private final String name;

	private final int price;

	private MenuOptionResponse(
		String name,
		int price
	) {
		this.name = name;
		this.price = price;
	}

	public static MenuOptionResponse from(MenuOption menuOption) {
		return new MenuOptionResponse(
			menuOption.getName(),
			menuOption.getPrice()
		);
	}
}
