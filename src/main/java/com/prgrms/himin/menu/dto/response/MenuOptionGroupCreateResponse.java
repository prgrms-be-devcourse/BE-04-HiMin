package com.prgrms.himin.menu.dto.response;

import com.prgrms.himin.menu.domain.MenuOptionGroup;

public record MenuOptionGroupCreateResponse(
	Long menuOptionGroupId,
	String name
) {

	public static MenuOptionGroupCreateResponse from(MenuOptionGroup menuOptionGroupEntity) {
		return new MenuOptionGroupCreateResponse(menuOptionGroupEntity.getId(), menuOptionGroupEntity.getName());
	}
}
