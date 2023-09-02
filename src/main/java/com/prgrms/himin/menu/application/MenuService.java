package com.prgrms.himin.menu.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.prgrms.himin.menu.domain.Menu;
import com.prgrms.himin.menu.domain.MenuOption;
import com.prgrms.himin.menu.domain.MenuOptionGroup;
import com.prgrms.himin.menu.domain.MenuOptionGroupRepository;
import com.prgrms.himin.menu.domain.MenuOptionRepository;
import com.prgrms.himin.menu.domain.MenuRepository;
import com.prgrms.himin.menu.domain.MenuStatus;
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

	@Transactional(readOnly = false)
	public MenuCreateResponse createMenu(Long shopId, MenuCreateRequest request) {
		Shop shop = shopRepository.findById(shopId)
			.orElseThrow(
				() -> new RuntimeException("존재 하지 않는 가게 id 입니다.")
			);
		Menu menu = request.toEntity();
		menu.attachShop(shop);
		Menu savedMenuEntity = menuRepository.save(menu);
		return MenuCreateResponse.from(savedMenuEntity);
	}

	@Transactional(readOnly = false)
	public MenuOptionGroupCreateResponse createMenuOptionGroup(
		Long shopId,
		Long menuId,
		MenuOptionGroupCreateRequest request
	) {
		MenuOptionGroup menuOptionGroupEntity = request.toEntity();
		Menu menu = menuRepository.findById(menuId)
			.orElseThrow(
				() -> new RuntimeException("존재 하지 않는 메뉴 id 입니다.")
			);
		checkShopId(shopId, menu);
		MenuOptionGroup savedMenuOptionGroupEntity = menuOptionGroupRepository.save(menuOptionGroupEntity);
		savedMenuOptionGroupEntity.attachMenu(menu);
		return MenuOptionGroupCreateResponse.from(savedMenuOptionGroupEntity);
	}

	@Transactional(readOnly = false)
	public MenuOptionCreateResponse createMenuOption(
		Long shopId,
		Long menuId,
		Long menuOptionGroupId,
		MenuOptionCreateRequest request
	) {
		MenuOption menuOptionEntity = request.toEntity();
		MenuOptionGroup menuOptionGroup = menuOptionGroupRepository.findById(menuOptionGroupId)
			.orElseThrow(
				() -> new RuntimeException("존재 하지 않는 메뉴 옵션 그룹 id 입니다.")
			);

		Menu menu = menuOptionGroup.getMenu();
		if (!menuId.equals(menu.getId())) {
			throw new RuntimeException("잘못된 메뉴 id 입니다.");
		}

		checkShopId(shopId, menu);

		menuOptionEntity.attachMenuOptionGroup(menuOptionGroup);
		MenuOption savedMenuOption = menuOptionRepository.save(menuOptionEntity);
		return MenuOptionCreateResponse.from(savedMenuOption);
	}

	public MenuResponse getMenu(
		Long shopId,
		Long menuId
	) {
		Menu menu = menuRepository.findById(menuId)
			.orElseThrow(() -> new RuntimeException("메뉴를 찾을 수 없습니다."));

		checkShopId(shopId, menu);

		return MenuResponse.from(menu);
	}

	@Transactional(readOnly = false)
	public void updateMenu(
		Long shopId,
		Long menuId,
		MenuUpdateRequest.Info request
	) {
		Menu menu = menuRepository.findById(menuId)
			.orElseThrow(() -> new RuntimeException("메뉴를 찾을 수 없습니다."));

		checkShopId(
			shopId,
			menu
		);
		menu.updateMenuInfo(
			request.name(),
			request.price()
		);
	}

	@Transactional(readOnly = false)
	public void changeMenuStatus(
		Long shopId,
		Long menuId,
		MenuUpdateRequest.Status request
	) {
		Menu menu = menuRepository.findById(menuId)
			.orElseThrow(() -> new RuntimeException("메뉴를 찾을 수 없습니다."));

		checkShopId(
			shopId,
			menu
		);
		MenuStatus status = request.status();
		menu.updateStatus(status);
	}

	@Transactional(readOnly = false)
	public void updateMenuOptionGroup(
		Long shopId,
		Long menuId,
		Long menuOptionGroupId,
		MenuOptionGroupUpdateRequest request
	) {
		Menu menu = menuRepository.findById(menuId)
			.orElseThrow(() -> new RuntimeException("메뉴를 찾을 수 없습니다."));

		checkShopId(
			shopId,
			menu
		);

		MenuOptionGroup menuOptionGroup = menuOptionGroupRepository.findById(menuOptionGroupId)
			.orElseThrow(() -> new RuntimeException("메뉴 옵션 그룹 id를 찾을 수 없습니다."));

		checkMenuId(
			menuId,
			menuOptionGroup
		);

		String name = request.name();
		menuOptionGroup.updateName(name);
	}

	@Transactional(readOnly = false)
	public void updateMenuOption(
		Long shopId,
		Long menuId,
		Long menuOptionGroupId,
		Long optionId,
		MenuOptionUpdateRequest request
	) {
		Menu menu = menuRepository.findById(menuId)
			.orElseThrow(() -> new RuntimeException("메뉴를 찾을 수 없습니다."));

		checkShopId(
			shopId,
			menu
		);

		MenuOptionGroup menuOptionGroup = menuOptionGroupRepository.findById(menuOptionGroupId)
			.orElseThrow(() -> new RuntimeException("메뉴 옵션 그룹 id를 찾을 수 없습니다."));

		checkMenuId(
			menuId,
			menuOptionGroup
		);

		MenuOption menuOption = menuOptionRepository.findById(optionId)
			.orElseThrow(() -> new RuntimeException("메뉴 옵션 id를 찾을 수 없습니다."));

		checkMenuOptionGroupId(
			menuOptionGroupId,
			menuOption
		);

		menuOption.updateOptionInfo(
			request.name(),
			request.price()
		);
	}

	@Transactional(readOnly = false)
	public void deleteMenu(
		Long shopId,
		Long menuId
	) {
		Menu menu = menuRepository.findById(menuId)
			.orElseThrow(() -> new RuntimeException("메뉴를 찾을 수 없습니다."));

		checkShopId(
			shopId,
			menu
		);

		menuRepository.delete(menu);
	}

	@Transactional(readOnly = false)
	public void deleteMenuOptionGroup(
		Long shopId,
		Long menuId,
		Long menuOptionGroupId
	) {
		Menu menu = menuRepository.findById(menuId)
			.orElseThrow(() -> new RuntimeException("메뉴를 찾을 수 없습니다."));

		checkShopId(
			shopId,
			menu
		);

		MenuOptionGroup menuOptionGroup = menuOptionGroupRepository.findById(menuOptionGroupId)
			.orElseThrow(() -> new RuntimeException("메뉴 옵션 그룹 id를 찾을 수 없습니다."));

		checkMenuId(
			menuId,
			menuOptionGroup
		);

		menuOptionGroupRepository.delete(menuOptionGroup);
	}

	@Transactional(readOnly = false)
	public void deleteMenuOption(
		Long shopId,
		Long menuId,
		Long menuOptionGroupId,
		Long optionId
	) {
		Menu menu = menuRepository.findById(menuId)
			.orElseThrow(() -> new RuntimeException("메뉴를 찾을 수 없습니다."));

		checkShopId(
			shopId,
			menu
		);

		MenuOptionGroup menuOptionGroup = menuOptionGroupRepository.findById(menuOptionGroupId)
			.orElseThrow(() -> new RuntimeException("메뉴 옵션 그룹 id를 찾을 수 없습니다."));

		checkMenuId(
			menuId,
			menuOptionGroup
		);

		MenuOption menuOption = menuOptionRepository.findById(optionId)
			.orElseThrow(() -> new RuntimeException("메뉴 옵션 id를 찾을 수 없습니다."));

		checkMenuOptionGroupId(
			menuOptionGroupId,
			menuOption
		);

		menuOptionRepository.delete(menuOption);
	}

	private void checkShopId(
		Long shopId,
		Menu menu
	) {
		Shop shop = menu.getShop();
		if (!shopId.equals(shop.getShopId())) {
			throw new RuntimeException("잘못된 가게 id 입니다.");
		}
	}

	private void checkMenuId(
		Long menuId,
		MenuOptionGroup menuOptionGroup
	) {
		Menu menu = menuOptionGroup.getMenu();
		if (!menuId.equals(menu.getId())) {
			throw new RuntimeException("잘못된 메뉴 id 입니다.");
		}
	}

	private void checkMenuOptionGroupId(
		Long menuOptionGroupId,
		MenuOption menuOption
	) {
		MenuOptionGroup menuOptionGroup = menuOption.getMenuOptionGroup();
		if (!menuOptionGroupId.equals(menuOptionGroup.getId())) {
			throw new RuntimeException("잘못된 메뉴 옵션 그룹 id 입니다.");
		}
	}
}
