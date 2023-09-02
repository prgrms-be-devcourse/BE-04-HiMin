package com.prgrms.himin.menu.dto.request;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.prgrms.himin.menu.domain.Menu;

public record MenuCreateRequest(
	@Size(max = 30, message = "메뉴 이름은 최대 30글자 입니다.")
	@NotBlank(message = "메뉴 이름이 비어있으면 안됩니다.")
	String name,

	@Min(value = 0, message = "price는 음수가 되면 안됩니다.")
	int price,

	@NotNull(message = "popularity가 null값이면 안됩니다.")
	boolean popularity
) {

	public Menu toEntity() {
		return Menu.builder()
			.name(this.name)
			.price(this.price)
			.popularity(this.popularity)
			.build();
	}
}
