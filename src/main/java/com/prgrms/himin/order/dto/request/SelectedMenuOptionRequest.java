package com.prgrms.himin.order.dto.request;

import java.util.List;

import javax.validation.constraints.NotNull;

public record SelectedMenuOptionRequest(
	@NotNull(message = "메뉴옵션그룹id가 null이면 안됩니다.")
	Long menuOptionGroupId,

	@NotNull(message = "메뉴옵션리스트가 null이면 안됩니다.")
	List<@NotNull Long> selectedMenuOptions
) {
}
