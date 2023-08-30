package com.prgrms.himin.menu.dto.response;

import com.prgrms.himin.menu.domain.MenuOptionGroup;

import lombok.Getter;

@Getter
public final class MenuOptionGroupCreateResponse {

	private final String name;

	public MenuOptionGroupCreateResponse(String name) {
		this.name = name;
	}

	public static MenuOptionGroupCreateResponse from(MenuOptionGroup menuOptionGroupEntity) {
		return new MenuOptionGroupCreateResponse(
			menuOptionGroupEntity.getName()
		);
	}
}
