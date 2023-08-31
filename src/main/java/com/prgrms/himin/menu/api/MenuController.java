package com.prgrms.himin.menu.api;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.prgrms.himin.menu.application.MenuService;
import com.prgrms.himin.menu.dto.request.MenuCreateRequest;
import com.prgrms.himin.menu.dto.request.MenuOptionCreateRequest;
import com.prgrms.himin.menu.dto.request.MenuOptionGroupCreateRequest;
import com.prgrms.himin.menu.dto.response.MenuCreateResponse;
import com.prgrms.himin.menu.dto.response.MenuOptionCreateResponse;
import com.prgrms.himin.menu.dto.response.MenuOptionGroupCreateResponse;
import com.prgrms.himin.menu.dto.response.MenuResponse;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/shops")
public class MenuController {

	private final MenuService menuService;

	@PostMapping("/{shopId}/menus")
	public ResponseEntity<MenuCreateResponse> createMenu(
		@PathVariable Long shopId,
		@Valid @RequestBody MenuCreateRequest request) {
		MenuCreateResponse response = menuService.createMenu(
			shopId,
			request
		);
		return ResponseEntity.ok(response);
	}

	@PostMapping("/{shopId}/menus/{menuId}/option-group")
	public ResponseEntity<MenuOptionGroupCreateResponse> createMenuOptionGroup(
		@PathVariable Long shopId,
		@PathVariable Long menuId,
		@RequestBody MenuOptionGroupCreateRequest request
	) {
		MenuOptionGroupCreateResponse response = menuService.createMenuOptionGroup(
			shopId,
			menuId,
			request
		);
		return ResponseEntity.ok(response);
	}

	@PostMapping("/{shopId}/menus/{menuId}/option-group/{menuOptionGroupId}/options")
	public ResponseEntity<MenuOptionCreateResponse> createMenuOption(
		@PathVariable Long shopId,
		@PathVariable Long menuId,
		@PathVariable Long menuOptionGroupId,
		@RequestBody MenuOptionCreateRequest request
	) {
		MenuOptionCreateResponse response = menuService.createMenuOption(
			shopId,
			menuId,
			menuOptionGroupId,
			request
		);
		return ResponseEntity.ok(response);
	}

	@GetMapping("/{shopId}/menus/{menuId}")
	public ResponseEntity<MenuResponse> getMenu(
		@PathVariable Long shopId,
		@PathVariable Long menuId
	) {
		MenuResponse menuResponse = menuService.getMenu(
			shopId,
			menuId
		);
		return ResponseEntity.ok(menuResponse);
	}
}
