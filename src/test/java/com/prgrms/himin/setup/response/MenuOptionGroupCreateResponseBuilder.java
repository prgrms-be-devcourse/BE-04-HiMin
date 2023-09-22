package com.prgrms.himin.setup.response;

import com.prgrms.himin.menu.dto.response.MenuOptionGroupCreateResponse;

public class MenuOptionGroupCreateResponseBuilder {

	public static MenuOptionGroupCreateResponse successBuild() {
		return new MenuOptionGroupCreateResponse(1L, "추가 메뉴");
	}
}
