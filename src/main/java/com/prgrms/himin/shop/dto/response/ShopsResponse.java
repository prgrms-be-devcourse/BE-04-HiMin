package com.prgrms.himin.shop.dto.response;

import java.util.List;

import com.prgrms.himin.shop.domain.ShopSort;

public record ShopsResponse(
	List<ShopResponse> shopResponses,
	int size,
	Long nextCursor,
	ShopSort sort,
	boolean isLast
) {
}
