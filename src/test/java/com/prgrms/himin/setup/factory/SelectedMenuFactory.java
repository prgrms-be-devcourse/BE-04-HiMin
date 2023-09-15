package com.prgrms.himin.setup.factory;

import java.util.ArrayList;
import java.util.List;

import com.prgrms.himin.menu.domain.Menu;
import com.prgrms.himin.menu.domain.MenuOption;
import com.prgrms.himin.menu.domain.MenuOptionGroup;
import com.prgrms.himin.order.dto.request.SelectedMenuOptionRequest;
import com.prgrms.himin.order.dto.request.SelectedMenuRequest;

public class SelectedMenuFactory {

	private List<SelectedMenuRequest> selectedMenuRequests;

	private List<SelectedMenuOptionRequest> selectedMenuOptionRequests;

	private List<Long> selectedMenuOptions;

	private OrderDomainTestDependencyFactory orderDomainTestDependencyFactory;

	private boolean isFirstInit;

	public SelectedMenuFactory(
		OrderDomainTestDependencyFactory orderDomainTestDependencyFactory
	) {
		selectedMenuRequests = new ArrayList<>();
		selectedMenuOptionRequests = new ArrayList<>();
		selectedMenuOptions = new ArrayList<>();
		this.orderDomainTestDependencyFactory = orderDomainTestDependencyFactory;
	}

	public void initSelectedMenuFactory() {
		if (isFirstInit) {
			return;
		}
		isFirstInit = true;
		initSelectedMenuOptions(orderDomainTestDependencyFactory.getMenuOptions());
		initSelectedMenuOptionGroupRequests(orderDomainTestDependencyFactory.getMenuOptionGroups());
		initSelectedMenuRequests(orderDomainTestDependencyFactory.getMenus());
	}

	private void initSelectedMenuRequests(List<Menu> menus) {
		for (Menu menu : menus) {
			SelectedMenuRequest selectedMenuRequest = new SelectedMenuRequest(
				menu.getId(),
				10,
				selectedMenuOptionRequests
			);
			selectedMenuRequests.add(selectedMenuRequest);
		}
	}

	private void initSelectedMenuOptionGroupRequests(List<MenuOptionGroup> menuOptionGroups) {
		for (MenuOptionGroup menuOptionGroup : menuOptionGroups) {
			SelectedMenuOptionRequest selectedMenuOptionRequest = new SelectedMenuOptionRequest(
				menuOptionGroup.getId(),
				selectedMenuOptions
			);
		}
	}

	private void initSelectedMenuOptions(List<MenuOption> menuOptions) {
		for (MenuOption menuOption : menuOptions) {
			selectedMenuOptions.add(menuOption.getId());
		}
	}

	public List<SelectedMenuRequest> getSelectedMenuRequests() {
		return selectedMenuRequests;
	}
}
