package com.prgrms.himin.order.dto.request;

import static com.prgrms.himin.order.domain.OrderItem.*;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public record SelectedMenuRequest(
	@NotNull(message = "menuId가 null이면 안됩니다.")
	Long menuId,

	@Min(value = MIN_ORDER_QUANTITY, message = "주문수량은 최소 {min}개입니다.")
	int quantity,

	@NotNull(message = "메뉴옵션요청이 null이면 안됩니다.")
	List<@Valid SelectedMenuOptionRequest> selectedMenuOptions
) {
}
