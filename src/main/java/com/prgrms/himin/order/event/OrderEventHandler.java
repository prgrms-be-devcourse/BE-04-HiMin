package com.prgrms.himin.order.event;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import com.prgrms.himin.order.application.OrderService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class OrderEventHandler {

	private final OrderService orderService;

	@EventListener(classes = DeliveryFinishedEvent.class)
	public void finishOrder(DeliveryFinishedEvent event) {
		orderService.finishOrder(event.getOrderId());
	}

	@EventListener(classes = StartedCookingEvent.class)
	public void startCooking(StartedCookingEvent event) {
		orderService.startCooking(
			event.getShopId(),
			event.getOrderId()
		);
	}

	@EventListener(classes = CookingFinishedEvent.class)
	public void finishCooking(CookingFinishedEvent event) {
		orderService.finishCooking(
			event.getShopId(),
			event.getOrderId()
		);
	}
}
