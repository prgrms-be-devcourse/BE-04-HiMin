package com.prgrms.himin.setup.domain;

import java.util.List;

import org.springframework.stereotype.Component;

import com.prgrms.himin.menu.domain.Menu;
import com.prgrms.himin.menu.domain.MenuRepository;
import com.prgrms.himin.shop.domain.Shop;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class MenuSetUp {

	private final MenuRepository menuRepository;

	public Menu saveOne(Shop shop) {
		final Menu menu = buildMenu("짜장면", 5000);
		menu.attachShop(shop);

		return menuRepository.save(menu);
	}

	public List<Menu> saveMany(Shop shop) {
		List<Menu> menus = getMenus(shop);

		return menuRepository.saveAll(menus);
	}

	public List<Menu> getMenus(Shop shop) {
		final Menu menu1 = buildMenu("짜장면", 5000);
		menu1.attachShop(shop);
		final Menu menu2 = buildMenu("짬뽕", 6000);
		menu2.attachShop(shop);
		final Menu menu3 = buildMenu("탕수육", 15000);
		menu3.attachShop(shop);

		return List.of(
			menu1,
			menu2,
			menu3
		);
	}

	private Menu buildMenu(
		String name,
		int price
	) {
		return new Menu(
			name,
			price,
			true
		);
	}
}
