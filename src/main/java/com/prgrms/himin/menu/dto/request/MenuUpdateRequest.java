package com.prgrms.himin.menu.dto.request;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.prgrms.himin.menu.domain.MenuStatus;

import lombok.Getter;

public record MenuUpdateRequest() {

	public record Info(
		@Size(max = 30, message = "메뉴 이름은 최대 20글자 입니다.")
		@NotBlank(message = "메뉴 이름이 비어있으면 안됩니다.")
		String name,

		@Min(value = 0, message = "price는 음수가 되면 안됩니다.")
		int price
	) {
	}

	@Getter
	public record Status(
		MenuStatus status
	) {
	}
}
