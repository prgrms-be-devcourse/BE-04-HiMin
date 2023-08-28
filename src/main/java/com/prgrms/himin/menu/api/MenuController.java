package com.prgrms.himin.menu.api;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.prgrms.himin.menu.application.MenuService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/menus")
public class MenuController {

	private final MenuService menuService;
}
