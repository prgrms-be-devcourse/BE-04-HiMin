package com.prgrms.himin.setup.domain;

import org.springframework.stereotype.Component;

import com.prgrms.himin.menu.domain.Menu;
import com.prgrms.himin.order.domain.OrderItem;

@Component
public class OrderItemSetUp {

	public OrderItem makeOne(
		Menu menu,
		int quantity
	) {
		return new OrderItem(menu, quantity);
	}

	public OrderItem makeOne(
		Menu menu
	) {
		return new OrderItem(menu, 5);
	}
}
