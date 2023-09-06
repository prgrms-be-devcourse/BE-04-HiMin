package com.prgrms.himin.menu.domain;

import static com.prgrms.himin.global.error.exception.ErrorCode.*;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.prgrms.himin.global.error.exception.InvalidValueException;
import com.prgrms.himin.shop.domain.Shop;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MenuValidator {

	public void validateShopId(
		Long shopId,
		Menu menu
	) {
		Shop shop = menu.getShop();
		if (!shopId.equals(shop.getShopId())) {
			throw new InvalidValueException(SHOP_BAD_REQUEST);
		}
	}

	public void validateMenuId(
		Long menuId,
		MenuOptionGroup menuOptionGroup
	) {
		Menu menu = menuOptionGroup.getMenu();
		if (!menuId.equals(menu.getId())) {
			throw new InvalidValueException(MENU_BAD_REQUEST);
		}
	}

	public void validateMenuOptionGroupId(
		Long menuOptionGroupId,
		MenuOption menuOption
	) {
		MenuOptionGroup menuOptionGroup = menuOption.getMenuOptionGroup();
		if (!menuOptionGroupId.equals(menuOptionGroup.getId())) {
			throw new InvalidValueException(MENU_OPTION_GROUP_BAD_REQUEST);
		}
	}
}
