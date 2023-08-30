package com.prgrms.himin.menu.dto.request;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.prgrms.himin.menu.domain.Menu;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public final class MenuCreateRequest {

	@NotBlank(message = "name이 비어있으면 안됩니다.")
	private final String name;

	@Min(value = 0, message = "price는 0 이상이어야 합니다.")
	private final int price;

	@NotNull(message = "popularity가 null값이면 안됩니다.")
	private final boolean popularity;

	public Menu toEntity() {
		return Menu.builder()
			.name(this.name)
			.price(this.price)
			.popularity(this.popularity)
			.build();
	}
}
