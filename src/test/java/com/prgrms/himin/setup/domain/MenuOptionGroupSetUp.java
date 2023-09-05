package com.prgrms.himin.setup.domain;

import java.util.List;

import org.springframework.stereotype.Component;

import com.prgrms.himin.menu.domain.Menu;
import com.prgrms.himin.menu.domain.MenuOptionGroup;
import com.prgrms.himin.menu.domain.MenuOptionGroupRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class MenuOptionGroupSetUp {

	private final MenuOptionGroupRepository menuOptionGroupRepository;

	public MenuOptionGroup saveOne(Menu menu) {
		MenuOptionGroup menuOptionGroup = buildMenuOptionGroup("추가 메뉴");
		menuOptionGroup.attachMenu(menu);

		return menuOptionGroupRepository.save(menuOptionGroup);
	}

	public List<MenuOptionGroup> saveMany(Menu menu) {
		List<MenuOptionGroup> menuOptionGroups = getMenuOptionGroups(menu);

		return menuOptionGroupRepository.saveAll(menuOptionGroups);
	}

	private List<MenuOptionGroup> getMenuOptionGroups(Menu menu) {
		MenuOptionGroup menuOptionGroup1 = buildMenuOptionGroup("추가 메뉴");
		menuOptionGroup1.attachMenu(menu);

		MenuOptionGroup menuOptionGroup2 = buildMenuOptionGroup("맵기 선택");
		menuOptionGroup2.attachMenu(menu);

		MenuOptionGroup menuOptionGroup3 = buildMenuOptionGroup("면 선택");
		menuOptionGroup3.attachMenu(menu);

		return List.of(
			menuOptionGroup1,
			menuOptionGroup2,
			menuOptionGroup3
		);
	}

	private MenuOptionGroup buildMenuOptionGroup(String name) {
		return new MenuOptionGroup(name);
	}
}
