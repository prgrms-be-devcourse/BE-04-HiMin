package com.prgrms.himin.menu.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.prgrms.himin.menu.domain.Menu;
import com.prgrms.himin.menu.domain.MenuOptionGroup;
import com.prgrms.himin.menu.domain.MenuOptionGroupRepository;
import com.prgrms.himin.menu.domain.MenuRepository;
import com.prgrms.himin.menu.dto.request.MenuCreateRequest;
import com.prgrms.himin.menu.dto.request.MenuOptionGroupCreateRequest;
import com.prgrms.himin.menu.dto.response.MenuCreateResponse;
import com.prgrms.himin.menu.dto.response.MenuOptionGroupCreateResponse;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MenuService {

	private final MenuRepository menuRepository;

	private final MenuOptionGroupRepository menuOptionGroupRepository;

	@Transactional(readOnly = false)
	public MenuCreateResponse createMenu(MenuCreateRequest request) {
		Menu menuEntity = request.toEntity();
		Menu savedMenuEntity = menuRepository.save(menuEntity);
		return MenuCreateResponse.from(savedMenuEntity);
	}

	@Transactional(readOnly = false)
	public MenuOptionGroupCreateResponse createMenuOptionGroup(
		Long menuId,
		MenuOptionGroupCreateRequest request
	) {
		MenuOptionGroup menuOptionGroupEntity = request.toEntity();
		Menu menu = menuRepository.findById(menuId)
			.orElseThrow(
				() -> new RuntimeException("존재 하지 않는 메뉴 id 입니다.")
			);
		MenuOptionGroup savedMenuOptionGroupEntity = menuOptionGroupRepository.save(menuOptionGroupEntity);
		savedMenuOptionGroupEntity.attachMenu(menu);
		return MenuOptionGroupCreateResponse.from(savedMenuOptionGroupEntity);
	}
}
