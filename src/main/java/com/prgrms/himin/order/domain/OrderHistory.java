package com.prgrms.himin.order.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.prgrms.himin.global.common.BaseEntity;
import com.prgrms.himin.global.error.exception.BusinessException;
import com.prgrms.himin.global.error.exception.ErrorCode;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@Table(name = "order_histories")
public class OrderHistory extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "order_history_id")
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "order_id", nullable = false)
	private Order order;

	@Enumerated(EnumType.STRING)
	@Column(name = "status", nullable = false)
	private OrderStatus orderStatus;

	private OrderHistory(
		Order order,
		OrderStatus orderStatus
	) {
		validateOrder(order);
		this.order = order;
		this.orderStatus = orderStatus;
	}

	public static OrderHistory createOrderHistory(Order order) {
		return new OrderHistory(
			order,
			OrderStatus.ORDERED
		);
	}

	public static OrderHistory createDeliveredOrderHistory(Order order) {
		return new OrderHistory(
			order,
			OrderStatus.DELIVERED
		);
	}

	public static OrderHistory createStartedCookingOrderHistory(Order order) {
		return new OrderHistory(
			order,
			OrderStatus.COOKING
		);
	}

	public static OrderHistory createCookingCompletedOrderHistory(Order order) {
		return new OrderHistory(
			order,
			OrderStatus.COOK_COMPLETE
		);
	}

	private void validateOrder(Order order) {
		if (order == null) {
			throw new BusinessException(ErrorCode.ORDER_BAD_REQUEST);
		}
	}
}
