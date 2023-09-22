package com.prgrms.himin.setup.response;

import com.prgrms.himin.menu.dto.response.MenuOptionCreateResponse;

public class MenuOptionCreateResponseBuilder {

	public static MenuOptionCreateResponse successBuild() {
		return new MenuOptionCreateResponse(1L, "김치", 1000);
	}
}
