package com.prgrms.himin.order.dto.response;

import java.util.List;

public record OrderResponses(

	List<OrderResponse> orderResponses,

	int size,

	Long nextCursor,

	boolean isLast
) {
}
