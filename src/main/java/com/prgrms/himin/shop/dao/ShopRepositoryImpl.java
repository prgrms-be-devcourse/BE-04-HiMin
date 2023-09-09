package com.prgrms.himin.shop.dao;

import static com.prgrms.himin.menu.domain.QMenu.*;
import static com.prgrms.himin.shop.domain.QShop.*;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.prgrms.himin.shop.domain.Category;
import com.prgrms.himin.shop.domain.Shop;
import com.prgrms.himin.shop.domain.ShopRepositoryCustom;
import com.prgrms.himin.shop.domain.ShopSort;
import com.prgrms.himin.shop.dto.request.ShopSearchCondition;
import com.querydsl.core.types.NullExpression;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class ShopRepositoryImpl implements ShopRepositoryCustom {

	private final JPAQueryFactory jpaQueryFactory;

	@Override
	public List<Shop> searchShops(
		ShopSearchCondition shopSearchCondition,
		int size,
		Long cursor,
		ShopSort sort
	) {
		return jpaQueryFactory
			.selectFrom(shop)
			.where(
				containsName(shopSearchCondition.name()),
				equalCategory(shopSearchCondition.category()),
				containsAddress(shopSearchCondition.address()),
				lowOrEqualDeliveryTip(shopSearchCondition.deliveryTip()),
				greaterThanCursor(cursor),
				containsMenuName(shopSearchCondition.menuName())
			)
			.leftJoin(shop.menus, menu).fetchJoin()
			.orderBy(orderBySort(sort))
			.limit(size + 1)
			.fetch();
	}

	private BooleanExpression containsName(String name) {
		return name != null ? shop.name.contains(name) : null;
	}

	private BooleanExpression equalCategory(Category category) {
		return category != null ? shop.category.eq(category) : null;
	}

	private BooleanExpression containsAddress(String address) {
		return address != null ? shop.address.contains(address) : null;
	}

	private BooleanExpression lowOrEqualDeliveryTip(Integer deliveryTip) {
		return deliveryTip != null ? shop.deliveryTip.loe(deliveryTip) : null;
	}

	private OrderSpecifier<?> orderBySort(ShopSort sort) {
		return sort != null ? sort.getOrderSpecifier() : OrderByNull.DEFAULT;
	}

	private Predicate greaterThanCursor(Long cursor) {
		return cursor != null ? shop.shopId.gt(cursor) : null;
	}

	private BooleanExpression containsMenuName(String menuName) {
		return menuName != null ? menu.name.contains(menuName) : null;
	}
}

class OrderByNull extends OrderSpecifier {

	static final OrderByNull DEFAULT = new OrderByNull();

	private OrderByNull() {
		super(Order.ASC, NullExpression.DEFAULT, NullHandling.Default);
	}
}
