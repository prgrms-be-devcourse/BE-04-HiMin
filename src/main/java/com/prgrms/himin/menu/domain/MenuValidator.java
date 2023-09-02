package com.prgrms.himin.menu.domain;

import static com.prgrms.himin.global.error.exception.ErrorCode.*;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.prgrms.himin.global.error.exception.EntityNotFoundException;
import com.prgrms.himin.global.error.exception.InvalidValueException;
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
			throw new InvalidValueException(SHOP_BAD_REQUEST);
		}
	}

	public void validateMenuId(
		Long menuId,
		Long menuOptionGroupID
	) {
		MenuOptionGroup menuOptionGroup = getMenuOptionGroup(menuOptionGroupID);
		if (!menuId.equals(menuOptionGroup.getMenu().getId())) {
			throw new InvalidValueException(MENU_BAD_REQUEST);
		}
	}

	public void validateMenuOptionGroupId(
		Long menuOptionGroupId,
		Long menuOptionId
	) {
		MenuOption menuOption = getMenuOption(menuOptionId);
		MenuOptionGroup menuOptionGroup = menuOption.getMenuOptionGroup();
		if (!menuOptionGroupId.equals(menuOptionGroup.getId())) {
			throw new InvalidValueException(MENU_OPTION_GROUP_BAD_REQUEST);
		}
	}

	private Menu getMenu(Long menuId) {
		return menuRepository.findById(menuId)
			.orElseThrow(
				() -> new EntityNotFoundException(MENU_NOT_FOUND)
			);
	}

	private MenuOptionGroup getMenuOptionGroup(Long menuOptionGroupId) {
		return menuOptionGroupRepository.findById(menuOptionGroupId)
			.orElseThrow(
				() -> new EntityNotFoundException(MENU_OPTION_GROUP_NOT_FOUND)
			);
	}

	private MenuOption getMenuOption(Long menuOptionId) {
		return menuOptionRepository.findById(menuOptionId)
			.orElseThrow(
				() -> new EntityNotFoundException(MENU_OPTION_NOT_FOUND)
			);
	}
}
