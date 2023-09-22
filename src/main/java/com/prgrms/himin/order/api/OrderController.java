package com.prgrms.himin.order.api;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.prgrms.himin.order.application.OrderService;
import com.prgrms.himin.order.dto.request.OrderCreateRequest;
import com.prgrms.himin.order.dto.request.OrderSearchCondition;
import com.prgrms.himin.order.dto.response.OrderResponse;
import com.prgrms.himin.order.dto.response.OrderResponses;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/orders")
public class OrderController {

	private final OrderService orderService;

	@PostMapping
	public ResponseEntity<OrderResponse> createOrder(@Valid @RequestBody OrderCreateRequest request) {
		OrderResponse response = orderService.createOrder(request);

		return ResponseEntity.ok(response);
	}

	@GetMapping("/{orderId}")
	public ResponseEntity<OrderResponse> getOrder(@PathVariable Long orderId) {
		OrderResponse response = orderService.getOrder(orderId);

		return ResponseEntity.ok(response);
	}

	@GetMapping("/list")
	public ResponseEntity<OrderResponses> getOrders(
		@RequestHeader Long memberId,
		@ModelAttribute OrderSearchCondition orderSearchCondition,
		@RequestParam(required = false, defaultValue = "10") int size,
		@RequestParam(required = false) Long cursor
	) {
		OrderResponses responses = orderService.getOrders(
			memberId,
			orderSearchCondition,
			size,
			cursor
		);

		return ResponseEntity.ok(responses);
	}
}
