package com.prgrms.himin.shop.api;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.prgrms.himin.shop.application.ShopService;
import com.prgrms.himin.shop.domain.Category;
import com.prgrms.himin.shop.domain.ShopSort;
import com.prgrms.himin.shop.dto.request.ShopCreateRequest;
import com.prgrms.himin.shop.dto.request.ShopUpdateRequest;
import com.prgrms.himin.shop.dto.response.ShopResponse;
import com.prgrms.himin.shop.dto.response.ShopsReponse;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/shops")
public class ShopController {

	private final ShopService shopService;

	@PostMapping
	public ResponseEntity<ShopResponse> createShop(@Valid @RequestBody ShopCreateRequest request) {
		ShopResponse response = shopService.createShop(request);

		return ResponseEntity.ok(response);
	}

	@GetMapping("/{shopId}")
	public ResponseEntity<ShopResponse> getShop(@PathVariable Long shopId) {
		ShopResponse response = shopService.getShop(shopId);

		return ResponseEntity.ok(response);
	}

	@GetMapping
	public ResponseEntity<ShopsReponse> getShops(
		@RequestParam(required = false) String name,
		@RequestParam(required = false) Category category,
		@RequestParam(required = false) String address,
		@RequestParam(required = false) Integer deliveryTip,
		@RequestParam(required = false, defaultValue = "10") int size,
		@RequestParam(required = false) Long cursor,
		@RequestParam(required = false) String sort
	) {
		ShopsReponse responses = shopService.getShops(
			name,
			category,
			address,
			deliveryTip,
			size,
			cursor,
			ShopSort.from(sort)
		);

		return ResponseEntity.ok(responses);
	}

	@PutMapping("/{shopId}")
	public ResponseEntity<Void> updateShop(
		@PathVariable Long shopId,
		@Valid @RequestBody ShopUpdateRequest.Info request
	) {
		shopService.updateShop(shopId, request);

		return ResponseEntity.noContent().build();
	}

	@PatchMapping("/{shopId}")
	public ResponseEntity<Void> changeShopStatus(
		@PathVariable Long shopId,
		@Valid @RequestBody ShopUpdateRequest.Status request
	) {
		shopService.changeShopStatus(shopId, request);

		return ResponseEntity.noContent().build();
	}

	@DeleteMapping("/{shopId}")
	public ResponseEntity<Void> deleteShop(@PathVariable Long shopId) {
		shopService.deleteShop(shopId);

		return ResponseEntity.ok().build();
	}
}
