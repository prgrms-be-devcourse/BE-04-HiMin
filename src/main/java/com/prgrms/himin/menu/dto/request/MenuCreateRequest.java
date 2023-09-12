package com.prgrms.himin.menu.dto.request;

import static com.prgrms.himin.menu.domain.Menu.*;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.prgrms.himin.menu.domain.Menu;

public record MenuCreateRequest(
	@Size(max = MAX_NAME_LENGTH, message = "메뉴 이름은 최대 {max}글자 입니다.")
	@NotBlank(message = "메뉴 이름이 비어있으면 안됩니다.")
	String name,

	@Min(value = MIN_PRICE, message = "price는 최소 {min}원입니다.")
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
