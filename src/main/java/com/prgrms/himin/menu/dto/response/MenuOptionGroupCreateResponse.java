package com.prgrms.himin.menu.dto.response;

import com.prgrms.himin.menu.domain.MenuOptionGroup;

import lombok.Getter;

@Getter
public final class MenuOptionGroupCreateResponse {

	private final Long menuOptionGroupId;

	private final String name;

	private MenuOptionGroupCreateResponse(
		Long menuOptionGroupId,
		String name
	) {
		this.menuOptionGroupId = menuOptionGroupId;
		this.name = name;
	}

	public static MenuOptionGroupCreateResponse from(MenuOptionGroup menuOptionGroupEntity) {
		return new MenuOptionGroupCreateResponse(
			menuOptionGroupEntity.getId(),
			menuOptionGroupEntity.getName()
		);
	}
}
