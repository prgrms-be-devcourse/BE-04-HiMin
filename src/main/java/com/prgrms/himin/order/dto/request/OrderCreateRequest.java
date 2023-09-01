package com.prgrms.himin.order.dto.request;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.lang.Nullable;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public final class OrderCreateRequest {

	@NotNull(message = "memberId가 null이면 안됩니다.")
	private final Long memberId;

	@NotNull(message = "shopId가 null이면 안됩니다.")
	private final Long shopId;

	@Size(max = 50, message = "주소는 최대 50글자입니다.")
	@NotBlank(message = "주소가 비어있으면 안됩니다.")
	private final String address;

	@Size(max = 20, message = "요청사항은 최대 20글자입니다.")
	@Nullable
	private final String requirement;

	@NotNull(message = "선택메뉴목록이 null이면 안됩니다.")
	private final List<@Valid SelectedMenuRequest> selectedMenus;
}
