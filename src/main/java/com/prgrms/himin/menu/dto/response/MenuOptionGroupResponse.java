package com.prgrms.himin.menu.dto.response;

import java.util.List;
import java.util.stream.Collectors;

import com.prgrms.himin.menu.domain.MenuOptionGroup;

public record MenuOptionGroupResponse(
	Long menuOptionGroupId,
	String name,
	List<MenuOptionResponse> menuOptionResponses
) {

	public static MenuOptionGroupResponse from(MenuOptionGroup menuOptionGroup) {
		List<MenuOptionResponse> menuOptionResponses = menuOptionGroup.getMenuOptions()
			.stream()
			.map(MenuOptionResponse::from)
			.collect(Collectors.toList());

		return new MenuOptionGroupResponse(
			menuOptionGroup.getId(),
			menuOptionGroup.getName(),
			menuOptionResponses
		);
	}
}
