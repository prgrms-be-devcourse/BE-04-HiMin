package com.prgrms.himin.setup.domain;

import java.util.List;

import org.springframework.stereotype.Component;

import com.prgrms.himin.menu.domain.MenuOption;
import com.prgrms.himin.menu.domain.MenuOptionGroup;
import com.prgrms.himin.menu.domain.MenuOptionRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class MenuOptionSetUp {

	private final MenuOptionRepository menuOptionRepository;

	public MenuOption saveOne(MenuOptionGroup menuOptionGroup) {
		MenuOption menuOption = buildMenuOption("소고기", 3000);
		menuOption.attachMenuOptionGroup(menuOptionGroup);

		return menuOptionRepository.save(menuOption);
	}

	public List<MenuOption> saveMany(MenuOptionGroup menuOptionGroup) {
		List<MenuOption> menuOptions = getMenuOptions(menuOptionGroup);

		return menuOptionRepository.saveAll(menuOptions);
	}

	private List<MenuOption> getMenuOptions(MenuOptionGroup menuOptionGroup) {
		MenuOption menuOption1 = buildMenuOption("돼지고기", 3000);
		menuOption1.attachMenuOptionGroup(menuOptionGroup);

		MenuOption menuOption2 = buildMenuOption("양고기", 10000);
		menuOption2.attachMenuOptionGroup(menuOptionGroup);

		MenuOption menuOption3 = buildMenuOption("닭고기", 3000);
		menuOption3.attachMenuOptionGroup(menuOptionGroup);

		MenuOption menuOption4 = buildMenuOption("말고기", 4000);
		menuOption4.attachMenuOptionGroup(menuOptionGroup);

		MenuOption menuOption5 = buildMenuOption("타조고기", 12000);
		menuOption5.attachMenuOptionGroup(menuOptionGroup);

		MenuOption menuOption6 = buildMenuOption("오리고기", 5000);
		menuOption6.attachMenuOptionGroup(menuOptionGroup);

		return List.of(
			menuOption1,
			menuOption2,
			menuOption3,
			menuOption4,
			menuOption5,
			menuOption6
		);
	}

	private MenuOption buildMenuOption(
		String name,
		int price
	) {
		return new MenuOption(name, price);
	}

}
