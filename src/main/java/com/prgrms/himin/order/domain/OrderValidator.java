package com.prgrms.himin.order.domain;

import org.springframework.stereotype.Component;

import com.prgrms.himin.global.error.exception.EntityNotFoundException;
import com.prgrms.himin.global.error.exception.ErrorCode;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class OrderValidator {

	private final OrderRepository orderRepository;

	public void validateOrderId(Long orderId) {
		if (!orderRepository.existsById(orderId)) {
			throw new EntityNotFoundException(ErrorCode.ORDER_NOT_FOUND);
		}
	}
}
