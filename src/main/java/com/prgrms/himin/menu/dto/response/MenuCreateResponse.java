package com.prgrms.himin.menu.dto.response;

import com.prgrms.himin.menu.domain.Menu;
import com.prgrms.himin.menu.domain.MenuStatus;

import lombok.Builder;

@Builder
public record MenuCreateResponse(
	Long menuId,
	String name,
	int price,
	boolean popularity,
	MenuStatus status
) {

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
