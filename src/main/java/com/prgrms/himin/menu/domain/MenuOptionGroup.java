package com.prgrms.himin.menu.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.prgrms.himin.global.error.exception.ErrorCode;
import com.prgrms.himin.global.error.exception.InvalidValueException;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "menu_option_group")
public class MenuOptionGroup {

	public static final int MAX_NAME_LENGTH = 30;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;

	@Column(name = "menu_group_name", nullable = false, length = 30)
	private String name;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "menu_id")
	private Menu menu;

	@OneToMany(mappedBy = "menuOptionGroup", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
	private List<MenuOption> menuOptions = new ArrayList<>();

	public MenuOptionGroup(
		String name
	) {
		validateName(name);
		this.name = name;
	}

	public void attachMenu(Menu menu) {
		if (this.menu != null) {
			this.menu.removeMenuOptionGroup(this);
		}
		this.menu = menu;

		menu.addMenuOptionGroup(this);
	}

	public void addMenuOption(MenuOption menuOption) {
		if (!menuOptions.contains(menuOption)) {
			this.menuOptions.add(menuOption);
		}
	}

	public void removeMenuOption(MenuOption menuOption) {
		this.menuOptions.remove(menuOption);
	}

	private void validateName(String name) {
		if (name == null || name.length() > MAX_NAME_LENGTH) {
			throw new InvalidValueException(ErrorCode.MENU_NAME_BAD_REQUEST);
		}
	}

	public void updateName(String name) {
		validateName(name);
		this.name = name;
	}

	public boolean contains(MenuOption menuOption) {
		return menuOptions.contains(menuOption);
	}
}
