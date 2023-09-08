package com.prgrms.himin.delivery.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.prgrms.himin.delivery.application.DeliveryService;
import com.prgrms.himin.delivery.dto.response.DeliveryResponse;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/deliveries")
public class DeliveryController {

	private final DeliveryService deliveryService;

	@PostMapping("/{orderId}")
	public ResponseEntity<DeliveryResponse> createDelivery(@PathVariable Long orderId) {
		DeliveryResponse response = deliveryService.createDelivery(orderId);

		return ResponseEntity.ok(response);
	}
}