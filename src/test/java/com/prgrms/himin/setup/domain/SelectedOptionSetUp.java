package com.prgrms.himin.setup.domain;

import java.util.List;

import org.springframework.stereotype.Component;

import com.prgrms.himin.menu.domain.MenuOption;
import com.prgrms.himin.order.domain.SelectedOption;

@Component
public class SelectedOptionSetUp {

	public List<SelectedOption> makeMany(List<MenuOption> menuOptions) {
		List<SelectedOption> selectedOptions = SelectedOption.from(menuOptions);

		return selectedOptions;
	}
}
