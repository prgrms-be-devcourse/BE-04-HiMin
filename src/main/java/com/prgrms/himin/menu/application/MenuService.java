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
	public MenuOptionGroupCreateResponse createMenuOptionGroup(Long menuId, MenuOptionGroupCreateRequest request) {
		MenuOptionGroup menuOptionGroupEntity = request.toEntity(menuId);
		MenuOptionGroup savedMenuOptionGroupEntity = menuOptionGroupRepository.save(menuOptionGroupEntity);
		return MenuOptionGroupCreateResponse.from(savedMenuOptionGroupEntity);
	}
}
