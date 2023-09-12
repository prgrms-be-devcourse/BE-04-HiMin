package com.prgrms.himin.menu.dto.request;

import static com.prgrms.himin.menu.domain.Menu.*;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.prgrms.himin.menu.domain.MenuStatus;

public record MenuUpdateRequest() {

	public record Info(
		@Size(max = MAX_NAME_LENGTH, message = "메뉴 이름은 최대 {max}글자 입니다.")
		@NotBlank(message = "메뉴 이름이 비어있으면 안됩니다.")
		String name,

		@Min(value = MIN_PRICE, message = "price는 최소 {min}원입니다.")
		int price
	) {
	}

	public record Status(
		MenuStatus status
	) {
	}
}
