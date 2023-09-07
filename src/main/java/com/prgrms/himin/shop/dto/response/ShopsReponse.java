package com.prgrms.himin.shop.dto.response;

import java.util.List;

import com.prgrms.himin.shop.domain.ShopSort;

public record ShopsReponse(
	List<ShopResponse> shopsReponses,
	int size,
	Long nextCursor,
	ShopSort sort
) {
}
