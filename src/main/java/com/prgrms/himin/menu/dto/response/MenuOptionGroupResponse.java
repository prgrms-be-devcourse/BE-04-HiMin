package com.prgrms.himin.menu.dto.response;

import java.util.List;
import java.util.stream.Collectors;

import com.prgrms.himin.menu.domain.MenuOptionGroup;

import lombok.Getter;

@Getter
public final class MenuOptionGroupResponse {

	private final String name;

	private final List<MenuOptionResponse> menuOptionResponses;

	private MenuOptionGroupResponse(
		String name,
		List<MenuOptionResponse> menuOptionResponses
	) {
		this.name = name;
		this.menuOptionResponses = menuOptionResponses;
	}

	public static MenuOptionGroupResponse from(MenuOptionGroup menuOptionGroup) {
		List<MenuOptionResponse> menuOptionResponses = menuOptionGroup.getMenuOptions()
			.stream()
			.map(MenuOptionResponse::from)
			.collect(Collectors.toList());

		return new MenuOptionGroupResponse(
			menuOptionGroup.getName(),
			menuOptionResponses
		);
	}
}
