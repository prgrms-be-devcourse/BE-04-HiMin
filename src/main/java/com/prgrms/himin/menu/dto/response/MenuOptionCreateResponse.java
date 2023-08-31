package com.prgrms.himin.menu.dto.response;

import com.prgrms.himin.menu.domain.MenuOption;

import lombok.Getter;

@Getter
public final class MenuOptionCreateResponse {

	private final String name;

	private final int price;

	private MenuOptionCreateResponse(
		String name,
		int price
	) {
		this.name = name;
		this.price = price;
	}

	public static MenuOptionCreateResponse from(MenuOption menuOption) {
		return new MenuOptionCreateResponse(
			menuOption.getName(),
			menuOption.getPrice()
		);
	}
}
