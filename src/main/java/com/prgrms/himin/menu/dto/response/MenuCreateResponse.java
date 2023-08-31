package com.prgrms.himin.menu.dto.response;

import com.prgrms.himin.menu.domain.Menu;
import com.prgrms.himin.menu.domain.MenuStatus;

import lombok.Builder;
import lombok.Getter;

@Getter
public final class MenuCreateResponse {

	private final Long menuId;

	private final String name;

	private final int price;

	private final boolean popularity;

	private final MenuStatus status;

	@Builder
	private MenuCreateResponse(
		Long menuId,
		String name,
		int price,
		boolean popularity,
		MenuStatus status
	) {
		this.menuId = menuId;
		this.name = name;
		this.price = price;
		this.popularity = popularity;
		this.status = status;
	}

	public static MenuCreateResponse from(Menu menu) {
		return MenuCreateResponse.builder()
			.menuId(menu.getId())
			.name(menu.getName())
			.price(menu.getPrice())
			.popularity(menu.isPopularity())
			.status(menu.getStatus())
			.build();
	}
}
