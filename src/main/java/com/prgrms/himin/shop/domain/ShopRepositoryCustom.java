package com.prgrms.himin.shop.domain;

import java.util.List;

import com.prgrms.himin.shop.dto.request.ShopSearchCondition;

public interface ShopRepositoryCustom {

	List<Shop> searchShops(
		ShopSearchCondition shopSearchCondition,
		int size,
		Long cursor,
		ShopSort sort
	);
}
