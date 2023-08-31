package com.prgrms.himin.menu.dto.response;

import java.util.List;
import java.util.stream.Collectors;

import com.prgrms.himin.menu.domain.Menu;
import com.prgrms.himin.menu.domain.MenuStatus;

import lombok.Builder;
import lombok.Getter;

@Getter
public final class MenuResponse {

	private final Long menuId;

	private final String name;

	private final int price;

	private final boolean popularity;

	private final MenuStatus status;

	private final List<MenuOptionGroupResponse> menuOptionGroupResponses;

	@Builder
	private MenuResponse(
		Long menuId,
		String name,
		int price,
		boolean popularity,
		MenuStatus status,
		List<MenuOptionGroupResponse> menuOptionGroupResponses
	) {
		this.menuId = menuId;
		this.name = name;
		this.price = price;
		this.popularity = popularity;
		this.status = status;
		this.menuOptionGroupResponses = menuOptionGroupResponses;
	}

	public static MenuResponse from(Menu menu) {
		List<MenuOptionGroupResponse> menuOptionGroupResponses = menu.getMenuOptionGroups()
			.stream()
			.map(MenuOptionGroupResponse::from)
			.collect(Collectors.toList());

		return MenuResponse.builder()
			.menuId(menu.getId())
			.name(menu.getName())
			.price(menu.getPrice())
			.popularity(menu.isPopularity())
			.status(menu.getStatus())
			.menuOptionGroupResponses(menuOptionGroupResponses)
			.build();
	}
}
