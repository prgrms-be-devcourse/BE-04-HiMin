package com.prgrms.himin.setup.request;

import java.util.List;
import java.util.stream.Collectors;

import com.prgrms.himin.menu.domain.MenuOption;
import com.prgrms.himin.order.dto.request.SelectedMenuOptionRequest;

public class SelectedMenuOptionRequestBuilder {

	public static SelectedMenuOptionRequest successBuild(List<MenuOption> selectedMenuOptions) {
		return new SelectedMenuOptionRequest(
			selectedMenuOptions.get(0).getMenuOptionGroup().getId(),
			selectedMenuOptions.stream().map(MenuOption::getId).collect(Collectors.toList())
		);
	}
}
