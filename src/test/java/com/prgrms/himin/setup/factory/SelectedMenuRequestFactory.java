package com.prgrms.himin.setup.factory;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.prgrms.himin.menu.domain.Menu;
import com.prgrms.himin.menu.domain.MenuOption;
import com.prgrms.himin.menu.domain.MenuOptionGroup;
import com.prgrms.himin.order.dto.request.SelectedMenuOptionRequest;
import com.prgrms.himin.order.dto.request.SelectedMenuRequest;
import com.prgrms.himin.setup.domain.MemberSetUp;
import com.prgrms.himin.setup.domain.MenuOptionGroupSetUp;
import com.prgrms.himin.setup.domain.MenuOptionSetUp;
import com.prgrms.himin.setup.domain.MenuSetUp;
import com.prgrms.himin.setup.domain.ShopSetUp;
import com.prgrms.himin.setup.request.SelectedMenuOptionRequestBuilder;
import com.prgrms.himin.setup.request.SelectedMenuRequestBuilder;
import com.prgrms.himin.shop.domain.Shop;

@Component
public class SelectedMenuRequestFactory {

	@Autowired
	MemberSetUp memberSetUp;

	@Autowired
	ShopSetUp shopSetUp;

	@Autowired
	MenuSetUp menuSetUp;

	@Autowired
	MenuOptionGroupSetUp menuOptionGroupSetUp;

	@Autowired
	MenuOptionSetUp menuOptionSetUp;

	private List<SelectedMenuRequest> selectedMenuRequests;

	public SelectedMenuRequestFactory(
	) {
		selectedMenuRequests = new ArrayList<>();
	}

	public void initSelectedMenuFactory(Shop shop) {
		initSelectedMenuRequests(shop);
	}

	private void initSelectedMenuRequests(Shop shop) {
		for (int i = 0; i < 3; i++) {
			Menu menu = menuSetUp.saveOne(shop);
			List<MenuOptionGroup> menuOptionGroups = menuOptionGroupSetUp.saveMany(menu);
			List<SelectedMenuOptionRequest> selectedMenuOptionRequests = new ArrayList<>();

			for (MenuOptionGroup menuOptionGroup : menuOptionGroups) {
				List<MenuOption> menuOptions = menuOptionSetUp.saveMany(menuOptionGroup);
				SelectedMenuOptionRequest selectedMenuOptionRequest = SelectedMenuOptionRequestBuilder
					.successBuild(menuOptions);
				selectedMenuOptionRequests.add(selectedMenuOptionRequest);
			}

			SelectedMenuRequest selectedMenuRequest = SelectedMenuRequestBuilder.successBuild(
				menu.getId(),
				selectedMenuOptionRequests
			);

			selectedMenuRequests.add(selectedMenuRequest);
		}
	}

	public List<SelectedMenuRequest> getSelectedMenuRequests() {
		return selectedMenuRequests;
	}
}
