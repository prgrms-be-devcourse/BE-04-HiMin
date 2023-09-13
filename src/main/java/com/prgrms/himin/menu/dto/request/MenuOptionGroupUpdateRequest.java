package com.prgrms.himin.menu.dto.request;

import static com.prgrms.himin.menu.domain.MenuOptionGroup.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public record MenuOptionGroupUpdateRequest(
	@Size(max = MAX_NAME_LENGTH, message = "메뉴 옵션 그룹 이름은 최대 {max}글자 입니다.")
	@NotBlank(message = "메뉴 옵션 그룹 이름은 비어있으면 안됩니다.")
	String name
) {
}
