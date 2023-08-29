package com.prgrms.himin.menu.api;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.prgrms.himin.menu.application.MenuService;
import com.prgrms.himin.menu.dto.request.MenuCreateRequest;
import com.prgrms.himin.menu.dto.response.MenuCreateResponse;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/menus")
public class MenuController {

	private final MenuService menuService;

	@PostMapping
	public ResponseEntity<MenuCreateResponse> createMenu(@Valid @RequestBody MenuCreateRequest menuCreateRequest) {
		MenuCreateResponse menuCreateResponse = menuService.createMenu(menuCreateRequest);
		return ResponseEntity.ok(menuCreateResponse);
	}
}
