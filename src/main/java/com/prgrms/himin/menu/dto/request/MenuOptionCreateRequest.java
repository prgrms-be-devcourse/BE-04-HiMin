package com.prgrms.himin.menu.dto.request;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.prgrms.himin.menu.domain.MenuOption;

import lombok.Getter;

@Getter
public final class MenuOptionCreateRequest {

	@Size(max = 30, message = "메뉴 옵션 이름은 최대 30글자 입니다.")
	@NotBlank(message = "메뉴 옵션 이름이 비어있으면 안됩니다.")
	private String name;

	@Min(value = 0, message = "price는 음수가 되면 안됩니다.")
	private int price;

	public MenuOption toEntity() {
		return new MenuOption(
			name,
			price
		);
	}
}
