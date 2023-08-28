package com.prgrms.himin.shop.api;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.prgrms.himin.shop.application.ShopService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/shops")
public class ShopController {

	private final ShopService shopService;
}
