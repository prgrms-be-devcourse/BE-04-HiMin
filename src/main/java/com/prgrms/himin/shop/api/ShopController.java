package com.prgrms.himin.shop.api;

import com.prgrms.himin.shop.application.ShopService;
import com.prgrms.himin.shop.dto.request.ShopCreateRequest;
import com.prgrms.himin.shop.dto.request.ShopUpdateRequest;
import com.prgrms.himin.shop.dto.response.ShopResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

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

    @PutMapping("/{shopId}")
    public ResponseEntity<Void> updateShop(
            @PathVariable Long shopId,
            @RequestBody ShopUpdateRequest.Info request
    ) {
        shopService.updateShop(shopId, request);
        return ResponseEntity.noContent().build();
    }
}
