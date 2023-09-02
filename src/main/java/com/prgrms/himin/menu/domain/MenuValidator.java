package com.prgrms.himin.menu.domain;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.prgrms.himin.shop.domain.Shop;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MenuValidator {

	private final MenuRepository menuRepository;

	private final MenuOptionGroupRepository menuOptionGroupRepository;

	private final MenuOptionRepository menuOptionRepository;

	public void validateShopId(
		Long shopId,
		Long menuId
	) {
		Menu menu = getMenu(menuId);
		Shop shop = menu.getShop();
		if (!shopId.equals(shop.getShopId())) {
			throw new RuntimeException("잘못된 가게 id 입니다.");
		}
	}

	public void validateMenuId(
		Long menuId,
		Long menuOptionGroupID
	) {
		MenuOptionGroup menuOptionGroup = getMenuOptionGroup(menuOptionGroupID);
		if (!menuId.equals(menuOptionGroup.getMenu().getId())) {
			throw new RuntimeException("잘못된 메뉴 id 입니다.");
		}
	}

	public void validateMenuOptionGroupId(
		Long menuOptionGroupId,
		Long menuOptionId
	) {
		MenuOption menuOption = getMenuOption(menuOptionId);
		MenuOptionGroup menuOptionGroup = menuOption.getMenuOptionGroup();
		if (!menuOptionGroupId.equals(menuOptionGroup.getId())) {
			throw new RuntimeException("잘못된 메뉴 옵션 그룹 id 입니다.");
		}
	}

	private Menu getMenu(Long menuId) {
		return menuRepository.findById(menuId)
			.orElseThrow(
				() -> new RuntimeException("존재 하지 않는 메뉴 id 입니다.")
			);
	}

	private MenuOptionGroup getMenuOptionGroup(Long menuOptionGroupId) {
		return menuOptionGroupRepository.findById(menuOptionGroupId)
			.orElseThrow(
				() -> new RuntimeException("존재 하지 않는 메뉴 옵션 그룹 id 입니다.")
			);
	}

	private MenuOption getMenuOption(Long menuOptionId) {
		return menuOptionRepository.findById(menuOptionId)
			.orElseThrow(
				() -> new RuntimeException("존재 하지 않는 메뉴 옵션 id 입니다.")
			);
	}
}
