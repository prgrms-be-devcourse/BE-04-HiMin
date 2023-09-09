package com.prgrms.himin.order.event;

import static org.springframework.transaction.annotation.Propagation.*;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import com.prgrms.himin.order.application.OrderService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class OrderEventHandler {

	private final OrderService orderService;

	@Transactional(propagation = REQUIRES_NEW)
	@TransactionalEventListener(
		classes = DeliveryFinishedEvent.class,
		phase = TransactionPhase.AFTER_COMMIT
	)
	public void finishOrder(DeliveryFinishedEvent event) {
		orderService.finishOrder(event.getOrderId());
	}
}
