package com.prgrms.himin.order.dto.request;

import java.util.List;

import javax.validation.constraints.NotNull;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public final class SelectedMenuOptionRequest {

	@NotNull(message = "메뉴옵션그룹id가 null이면 안됩니다.")
	private final Long menuOptionGroupId;

	@NotNull(message = "메뉴옵션리스트가 null이면 안됩니다.")
	private final List<@NotNull Long> selectedMenuOptions;
}
