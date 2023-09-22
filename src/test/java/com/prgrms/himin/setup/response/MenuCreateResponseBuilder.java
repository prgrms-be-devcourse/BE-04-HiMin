package com.prgrms.himin.setup.response;

import com.prgrms.himin.menu.domain.MenuStatus;
import com.prgrms.himin.menu.dto.response.MenuCreateResponse;

public class MenuCreateResponseBuilder {

	public static MenuCreateResponse successBuild() {
		return MenuCreateResponse.builder()
			.menuId(1L)
			.name("고기 국수")
			.price(5000)
			.popularity(false)
			.status(MenuStatus.UNSELLABLE)
			.build();
	}
}
