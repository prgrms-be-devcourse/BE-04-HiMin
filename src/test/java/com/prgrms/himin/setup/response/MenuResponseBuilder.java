package com.prgrms.himin.setup.response;

import java.util.List;

import com.prgrms.himin.menu.domain.MenuStatus;
import com.prgrms.himin.menu.dto.response.MenuOptionGroupResponse;
import com.prgrms.himin.menu.dto.response.MenuOptionResponse;
import com.prgrms.himin.menu.dto.response.MenuResponse;

public class MenuResponseBuilder {

	public static MenuResponse successBuild() {
		return MenuResponse.builder()
			.menuId(1L)
			.name("고기 국수")
			.price(5000)
			.popularity(false)
			.status(MenuStatus.UNSELLABLE)
			.menuOptionGroupResponses(
				List.of(
					new MenuOptionGroupResponse(
						1L,
						"리뷰 이벤트",
						List.of(
							new MenuOptionResponse(
								1L,
								"계란말이",
								100
							),
							new MenuOptionResponse(
								2L,
								"탕후루",
								300
							)
						)
					),
					new MenuOptionGroupResponse(
						2L,
						"추가 메뉴",
						List.of(
							new MenuOptionResponse(
								3L,
								"김치",
								500
							),
							new MenuOptionResponse(
								4L,
								"고수",
								1000
							)
						)
					)
				)
			)
			.build();
	}
}
