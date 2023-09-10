package com.prgrms.himin.order.domain;

import java.util.List;

import com.prgrms.himin.order.dto.request.OrderSearchCondition;

public interface OrderRepositoryCustom {

	List<Order> searchOrders(
		Long memberId,
		OrderSearchCondition orderSearchCondition,
		int size,
		Long cursor
	);
}
