package com.prgrms.himin.menu.dto.request;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class MenuOptionUpdateRequest {
	
	@Size(max = 30, message = "메뉴 이름은 최대 30글자 입니다.")
	@NotBlank(message = "메뉴 이름이 비어있으면 안됩니다.")
	private final String name;

	@Min(value = 0, message = "price는 음수가 되면 안됩니다.")
	private final int price;
}
