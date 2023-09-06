package com.prgrms.himin.setup.request;

import java.util.List;

import com.prgrms.himin.order.dto.request.SelectedMenuOptionRequest;
import com.prgrms.himin.order.dto.request.SelectedMenuRequest;

public class SelectedMenuRequestBuilder {

	public static SelectedMenuRequest successBuild(
		Long menuId,
		List<SelectedMenuOptionRequest> selectedMenuOptions
	) {
		return new SelectedMenuRequest(
			menuId,
			5,
			selectedMenuOptions
		);
	}
}
