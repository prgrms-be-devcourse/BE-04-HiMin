package com.prgrms.himin.menu.dto.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.prgrms.himin.menu.domain.MenuOptionGroup;

public record
MenuOptionGroupCreateRequest(
	@Size(max = 30, message = "메뉴 옵션 그룹 이름은 최대 30글자 입니다.")
	@NotBlank(message = "메뉴 옵션 그룹 이름은 비어있으면 안됩니다.")
	String name
) {

	public MenuOptionGroup toEntity() {
		return new MenuOptionGroup(name);
	}
}
