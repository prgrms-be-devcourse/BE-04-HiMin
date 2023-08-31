package com.prgrms.himin.order.api;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.prgrms.himin.order.application.OrderService;
import com.prgrms.himin.order.dto.request.OrderCreateRequest;
import com.prgrms.himin.order.dto.response.OrderResponse;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/orders")
public class OrderController {

	private final OrderService orderService;

	@PostMapping
	public ResponseEntity<OrderResponse> createOrder(@Valid @RequestBody OrderCreateRequest request) {
		return ResponseEntity.ok(orderService.createOrder(request));
	}
}
