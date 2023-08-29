package com.prgrms.himin.menu.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.prgrms.himin.menu.domain.Menu;
import com.prgrms.himin.menu.domain.MenuRepository;
import com.prgrms.himin.menu.dto.request.MenuCreateRequest;
import com.prgrms.himin.menu.dto.response.MenuCreateResponse;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MenuService {

	private final MenuRepository menuRepository;

	@Transactional(readOnly = false)
	public MenuCreateResponse createMenu(MenuCreateRequest menuCreateRequest) {
		Menu menuEntity = menuCreateRequest.toEntity();
		menuRepository.save(menuEntity);
		return MenuCreateResponse.from(menuEntity);
	}
}
