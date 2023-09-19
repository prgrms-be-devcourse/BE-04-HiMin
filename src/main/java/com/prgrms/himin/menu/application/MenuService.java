package com.prgrms.himin.menu.application;

import static com.prgrms.himin.global.error.exception.ErrorCode.*;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.prgrms.himin.global.error.exception.EntityNotFoundException;
import com.prgrms.himin.global.error.exception.ErrorCode;
import com.prgrms.himin.menu.domain.Menu;
import com.prgrms.himin.menu.domain.MenuOption;
import com.prgrms.himin.menu.domain.MenuOptionGroup;
import com.prgrms.himin.menu.domain.MenuOptionGroupRepository;
import com.prgrms.himin.menu.domain.MenuOptionRepository;
import com.prgrms.himin.menu.domain.MenuRepository;
import com.prgrms.himin.menu.domain.MenuStatus;
import com.prgrms.himin.menu.domain.MenuValidator;
import com.prgrms.himin.menu.dto.request.MenuCreateRequest;
import com.prgrms.himin.menu.dto.request.MenuOptionCreateRequest;
import com.prgrms.himin.menu.dto.request.MenuOptionGroupCreateRequest;
import com.prgrms.himin.menu.dto.request.MenuOptionGroupUpdateRequest;
import com.prgrms.himin.menu.dto.request.MenuOptionUpdateRequest;
import com.prgrms.himin.menu.dto.request.MenuUpdateRequest;
import com.prgrms.himin.menu.dto.response.MenuCreateResponse;
import com.prgrms.himin.menu.dto.response.MenuOptionCreateResponse;
import com.prgrms.himin.menu.dto.response.MenuOptionGroupCreateResponse;
import com.prgrms.himin.menu.dto.response.MenuResponse;
import com.prgrms.himin.shop.domain.Shop;
import com.prgrms.himin.shop.domain.ShopRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MenuService {

	private final MenuRepository menuRepository;

	private final MenuOptionGroupRepository menuOptionGroupRepository;

	private final MenuOptionRepository menuOptionRepository;

	private final ShopRepository shopRepository;

	private final MenuValidator menuValidator;

	@Transactional
	public MenuCreateResponse createMenu(
		Long shopId,
		MenuCreateRequest request
	) {
		Shop shop = shopRepository.findById(shopId)
			.orElseThrow(
				() -> new EntityNotFoundException(ErrorCode.SHOP_NOT_FOUND)
			);

		Menu menu = request.toEntity();
		menu.attachShop(shop);
		Menu savedMenuEntity = menuRepository.save(menu);

		return MenuCreateResponse.from(savedMenuEntity);
	}

	@Transactional
	public MenuOptionGroupCreateResponse createMenuOptionGroup(
		Long shopId,
		Long menuId,
		MenuOptionGroupCreateRequest request
	) {
		Menu menu = menuRepository.findById(menuId)
			.orElseThrow(
				() -> new EntityNotFoundException(ErrorCode.MENU_NOT_FOUND)
			);

		menuValidator.validateShopId(shopId, menu);

		MenuOptionGroup menuOptionGroupEntity = request.toEntity();
		MenuOptionGroup savedMenuOptionGroupEntity = menuOptionGroupRepository.save(menuOptionGroupEntity);
		savedMenuOptionGroupEntity.attachMenu(menu);

		return MenuOptionGroupCreateResponse.from(savedMenuOptionGroupEntity);
	}

	@Transactional
	public MenuOptionCreateResponse createMenuOption(
		Long shopId,
		Long menuId,
		Long menuOptionGroupId,
		MenuOptionCreateRequest request
	) {
		Menu menu = menuRepository.findById(menuId)
			.orElseThrow(
				() -> new EntityNotFoundException(MENU_NOT_FOUND)
			);

		MenuOptionGroup menuOptionGroup = menuOptionGroupRepository.findById(menuOptionGroupId)
			.orElseThrow(
				() -> new EntityNotFoundException(ErrorCode.MENU_OPTION_GROUP_NOT_FOUND)
			);

		menuValidator.validateShopId(shopId, menu);

		menuValidator.validateMenuId(menuId, menuOptionGroup);

		MenuOption menuOptionEntity = request.toEntity();

		menuOptionEntity.attachMenuOptionGroup(menuOptionGroup);
		MenuOption savedMenuOption = menuOptionRepository.save(menuOptionEntity);

		return MenuOptionCreateResponse.from(savedMenuOption);
	}

	public MenuResponse getMenu(
		Long shopId,
		Long menuId
	) {
		Menu menu = menuRepository.findById(menuId)
			.orElseThrow(
				() -> new EntityNotFoundException(ErrorCode.MENU_NOT_FOUND)
			);

		menuValidator.validateShopId(shopId, menu);

		return MenuResponse.from(menu);
	}

	@Transactional
	public void updateMenu(
		Long shopId,
		Long menuId,
		MenuUpdateRequest.Info request
	) {
		Menu menu = menuRepository.findById(menuId)
			.orElseThrow(
				() -> new EntityNotFoundException(ErrorCode.MENU_NOT_FOUND)
			);

		menuValidator.validateShopId(shopId, menu);

		menu.updateMenuInfo(request.name(), request.price());
	}

	@Transactional
	public void changeMenuStatus(
		Long shopId,
		Long menuId,
		MenuUpdateRequest.Status request
	) {
		Menu menu = menuRepository.findById(menuId)
			.orElseThrow(
				() -> new EntityNotFoundException(ErrorCode.MENU_NOT_FOUND)
			);

		menuValidator.validateShopId(shopId, menu);

		MenuStatus status = request.status();
		menu.updateStatus(status);
	}

	@Transactional
	public void updateMenuOptionGroup(
		Long shopId,
		Long menuId,
		Long menuOptionGroupId,
		MenuOptionGroupUpdateRequest request
	) {
		Menu menu = menuRepository.findById(menuId)
			.orElseThrow(
				() -> new EntityNotFoundException(ErrorCode.MENU_NOT_FOUND)
			);

		MenuOptionGroup menuOptionGroup = menuOptionGroupRepository.findById(menuOptionGroupId)
			.orElseThrow(
				() -> new EntityNotFoundException(MENU_OPTION_GROUP_NOT_FOUND)
			);

		menuValidator.validateShopId(shopId, menu);

		menuValidator.validateMenuId(menuId, menuOptionGroup);

		String name = request.name();
		menuOptionGroup.updateName(name);
	}

	@Transactional
	public void updateMenuOption(
		Long shopId,
		Long menuId,
		Long menuOptionGroupId,
		Long optionId,
		MenuOptionUpdateRequest request
	) {
		Menu menu = menuRepository.findById(menuId)
			.orElseThrow(
				() -> new EntityNotFoundException(ErrorCode.MENU_NOT_FOUND)
			);

		MenuOptionGroup menuOptionGroup = menuOptionGroupRepository.findById(menuOptionGroupId)
			.orElseThrow(
				() -> new EntityNotFoundException(MENU_OPTION_GROUP_NOT_FOUND)
			);

		MenuOption menuOption = menuOptionRepository.findById(optionId)
			.orElseThrow(
				() -> new EntityNotFoundException(MENU_OPTION_NOT_FOUND)
			);

		menuValidator.validateShopId(shopId, menu);

		menuValidator.validateMenuId(menuId, menuOptionGroup);

		menuValidator.validateMenuOptionGroupId(menuOptionGroupId, menuOption);

		menuOption.updateOptionInfo(request.name(), request.price());
	}

	@Transactional
	public void deleteMenu(
		Long shopId,
		Long menuId
	) {
		Menu menu = menuRepository.findById(menuId)
			.orElseThrow(
				() -> new EntityNotFoundException(MENU_NOT_FOUND)
			);

		menuValidator.validateShopId(shopId, menu);

		menuRepository.delete(menu);
	}

	@Transactional
	public void deleteMenuOptionGroup(
		Long shopId,
		Long menuId,
		Long menuOptionGroupId
	) {
		Menu menu = menuRepository.findById(menuId)
			.orElseThrow(
				() -> new EntityNotFoundException(MENU_NOT_FOUND)
			);

		MenuOptionGroup menuOptionGroup = menuOptionGroupRepository.findById(menuOptionGroupId)
			.orElseThrow(
				() -> new EntityNotFoundException(MENU_OPTION_GROUP_NOT_FOUND)
			);

		menuValidator.validateShopId(shopId, menu);

		menuValidator.validateMenuId(menuId, menuOptionGroup);

		menuOptionGroupRepository.delete(menuOptionGroup);
	}

	@Transactional
	public void deleteMenuOption(
		Long shopId,
		Long menuId,
		Long menuOptionGroupId,
		Long menuOptionID
	) {
		Menu menu = menuRepository.findById(menuId)
			.orElseThrow(
				() -> new EntityNotFoundException(MENU_NOT_FOUND)
			);

		MenuOptionGroup menuOptionGroup = menuOptionGroupRepository.findById(menuOptionGroupId)
			.orElseThrow(
				() -> new EntityNotFoundException(MENU_OPTION_GROUP_NOT_FOUND)
			);

		MenuOption menuOption = menuOptionRepository.findById(menuOptionID)
			.orElseThrow(
				() -> new EntityNotFoundException(MENU_OPTION_NOT_FOUND)
			);

		menuValidator.validateShopId(shopId, menu);

		menuValidator.validateMenuId(menuId, menuOptionGroup);

		menuValidator.validateMenuOptionGroupId(menuOptionGroupId, menuOption);

		menuOptionRepository.delete(menuOption);
	}

}
