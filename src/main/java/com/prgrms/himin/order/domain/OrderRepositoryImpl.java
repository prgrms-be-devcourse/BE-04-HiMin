package com.prgrms.himin.order.domain;

import static com.prgrms.himin.order.domain.QOrder.*;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.prgrms.himin.order.dto.request.OrderSearchCondition;
import com.querydsl.core.types.Predicate;
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
			.selectFrom(order)
			.where(
				order.member.id.eq(memberId),
				greaterThanCursor(cursor)
			)
			.limit(size + 1)
			.fetch();
	}

	private Predicate greaterThanCursor(Long cursor) {
		return cursor != null ? order.orderId.gt(cursor) : null;
	}
}
