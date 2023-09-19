package com.prgrms.himin.setup.factory;

import java.util.ArrayList;
import java.util.List;

import com.prgrms.himin.member.domain.Member;
import com.prgrms.himin.member.dto.request.MemberCreateRequest;
import com.prgrms.himin.menu.domain.Menu;
import com.prgrms.himin.menu.domain.MenuOption;
import com.prgrms.himin.menu.domain.MenuOptionGroup;
import com.prgrms.himin.menu.dto.request.MenuCreateRequest;
import com.prgrms.himin.menu.dto.request.MenuOptionCreateRequest;
import com.prgrms.himin.menu.dto.request.MenuOptionGroupCreateRequest;
import com.prgrms.himin.setup.request.MemberCreateRequestBuilder;
import com.prgrms.himin.setup.request.MenuCreateRequestBuilder;
import com.prgrms.himin.setup.request.MenuOptionCreateRequestBuilder;
import com.prgrms.himin.setup.request.MenuOptionGroupRequestBuilder;
import com.prgrms.himin.setup.request.ShopCreateRequestBuilder;
import com.prgrms.himin.shop.domain.Shop;
import com.prgrms.himin.shop.dto.request.ShopCreateRequest;

public class OrderDomainTestDependencyFactory {

	private Member member;

	private Shop shop;

	private List<Menu> menus;

	private List<MenuOptionGroup> menuOptionGroups;

	private List<MenuOption> menuOptions;

	private boolean isFirstInit;

	public OrderDomainTestDependencyFactory() {
		menus = new ArrayList<>();
		menuOptionGroups = new ArrayList<>();
		menuOptions = new ArrayList<>();
	}

	public void initByOrder() {
		if (isFirstInit) {
			return;
		}
		isFirstInit = true;

		initMember();
		initShop();
		initMenuOption();
		initMenuOptionGroup();
		initMenu();
	}

	private void initMember() {
		MemberCreateRequest memberCreateRequest = MemberCreateRequestBuilder.successBuild();
		member = memberCreateRequest.toEntity("password");
	}

	private void initShop() {
		ShopCreateRequest shopCreateRequest = ShopCreateRequestBuilder.successBuild();
		shop = shopCreateRequest.toEntity();
	}

	private void initMenu() {
		List<MenuCreateRequest> menuCreateRequests =
			MenuCreateRequestBuilder.successesBuild();

		int menuIdx = 0;
		int menuOptionIdx = 0;

		while (
			menuIdx < menus.size() &&
				menuOptionIdx < menuCreateRequests.size()
		) {
			MenuCreateRequest menuCreateRequest = menuCreateRequests.get(menuIdx++);
			Menu menu = menuCreateRequest.toEntity();

			menu.addMenuOptionGroup(menuOptionGroups.get(menuOptionIdx++));
		}
	}

	private void initMenuOptionGroup() {
		List<MenuOptionGroupCreateRequest> menuOptionGroupCreateRequests =
			MenuOptionGroupRequestBuilder.successesBuild();

		int menuOptionIdx = 0;
		int menuOptionGroupIdx = 0;

		while (
			menuOptionIdx + 1 < menuOptions.size() &&
				menuOptionGroupIdx < menuOptionGroupCreateRequests.size()
		) {
			MenuOptionGroupCreateRequest menuOptionGroupCreateRequest =
				menuOptionGroupCreateRequests.get(menuOptionGroupIdx++);
			MenuOptionGroup menuOptionGroup = menuOptionGroupCreateRequest.toEntity();

			menuOptionGroup.addMenuOption(menuOptions.get(menuOptionIdx++));
			menuOptionGroup.addMenuOption(menuOptions.get(menuOptionIdx++));

			menuOptionGroups.add(menuOptionGroup);
		}
	}

	private void initMenuOption() {
		List<MenuOptionCreateRequest> menuOptionCreateRequests
			= MenuOptionCreateRequestBuilder.successesBuild();

		menuOptionCreateRequests
			.stream()
			.map(MenuOptionCreateRequest::toEntity)
			.forEachOrdered(menuOption -> {
					menuOptions.add(menuOption);
				}
			);
	}

	public Member getMember() {
		return member;
	}

	public Shop getShop() {
		return shop;
	}

	public List<Menu> getMenus() {
		return menus;
	}

	public List<MenuOptionGroup> getMenuOptionGroups() {
		return menuOptionGroups;
	}

	public List<MenuOption> getMenuOptions() {
		return menuOptions;
	}
}
