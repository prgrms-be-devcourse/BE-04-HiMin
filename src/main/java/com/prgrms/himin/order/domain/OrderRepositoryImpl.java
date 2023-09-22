package com.prgrms.himin.order.domain;

import static com.prgrms.himin.order.domain.QOrder.*;
import static com.prgrms.himin.order.domain.QOrderHistory.*;
import static com.prgrms.himin.order.domain.QOrderItem.*;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.prgrms.himin.order.dto.request.OrderSearchCondition;
import com.prgrms.himin.shop.domain.Category;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class OrderRepositoryImpl implements OrderRepositoryCustom {

	private final JPAQueryFactory jpaQueryFactory;

	@Override
	public List<Order> searchOrders(
		Long memberId,
		OrderSearchCondition orderSearchCondition,
		int size,
		Long cursor
	) {
		return jpaQueryFactory
			.select(order)
			.from(order)
			.where(
				equalCategory(orderSearchCondition.categories()),
				equalOrderStatus(orderSearchCondition.orderStatuses()),
				betweenTime(orderSearchCondition.startTime(), orderSearchCondition.endTime()),
				greaterOrEqualThanCursor(cursor)
			)
			.leftJoin(orderHistory).on(orderHistory.order.eq(order)) // fetch가 아니라 필터 하기 위한 join
			.leftJoin(order.orderItems, orderItem)
			.fetchJoin()
			.limit(size)
			.fetch();
	}

	private Predicate greaterOrEqualThanCursor(Long cursor) {
		return cursor != null ? order.orderId.goe(cursor) : null;
	}

	private BooleanExpression betweenTime(LocalDateTime start, LocalDateTime end) {
		if (start == null || end == null) {
			return null;
		}

		return order.orderTime.between(start, end);
	}

	private BooleanBuilder equalCategory(List<Category> categories) {
		if (categories == null) {
			return null;
		}

		BooleanBuilder booleanBuilder = new BooleanBuilder();

		for (Category category : categories) {
			booleanBuilder.or(order.shop.category.eq(category));
		}

		return booleanBuilder;
	}

	private BooleanBuilder equalOrderStatus(List<OrderStatus> orderStatuses) {
		if (orderStatuses == null) {
			return null;
		}

		BooleanBuilder booleanBuilder = new BooleanBuilder();

		for (OrderStatus orderStatus : orderStatuses) {
			booleanBuilder.or(orderHistory.orderStatus.eq(orderStatus));
		}

		return booleanBuilder;
	}
}
