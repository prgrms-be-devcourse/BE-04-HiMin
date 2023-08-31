package com.prgrms.himin.menu.api;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
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

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/menus")
public class MenuController {

	private final MenuService menuService;

	@PostMapping
	public ResponseEntity<MenuCreateResponse> createMenu(@Valid @RequestBody MenuCreateRequest request) {
		MenuCreateResponse response = menuService.createMenu(request);
		return ResponseEntity.ok(response);
	}

	@PostMapping("/{menuId}/option-group")
	public ResponseEntity<MenuOptionGroupCreateResponse> createMenuOptionGroup(
		@PathVariable Long menuId,
		@RequestBody MenuOptionGroupCreateRequest request
	) {
		MenuOptionGroupCreateResponse response = menuService.createMenuOptionGroup(
			menuId,
			request
		);
		return ResponseEntity.ok(response);
	}

	@PostMapping("/{menuId}/option-group/{menuOptionGroupId}/options")
	public ResponseEntity<MenuOptionCreateResponse> createMenuOption(
		@PathVariable Long menuId,
		@PathVariable Long menuOptionGroupId,
		@RequestBody MenuOptionCreateRequest request
	) {
		MenuOptionCreateResponse response = menuService.createMenuOption(
			menuId,
			menuOptionGroupId,
			request
		);
		return ResponseEntity.ok(response);
	}
}
