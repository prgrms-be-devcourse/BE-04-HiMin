package com.prgrms.himin.menu.dto.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.Getter;

@Getter
public class MenuOptionGroupUpdateRequest {

	@Size(max = 30, message = "메뉴 옵션 그룹 이름은 최대 30글자 입니다.")
	@NotBlank(message = "메뉴 옵션 그룹 이름이 비어있으면 안됩니다.")
	private String name;
}
