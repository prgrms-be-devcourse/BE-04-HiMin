package com.prgrms.himin.menu.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "menu_options")
public class MenuOption {

	private static final int MIN_PRICE = 0;

	private static final int MAX_NAME_LENGTH = 30;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long menuOptionId;

	@Column(name = "name", nullable = false, length = 20)
	private String name;

	@Column(name = "price", nullable = false)
	private int price;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "menu_option_group_id")
	private MenuOptionGroup menuOptionGroup;

	public MenuOption(String name, int price) {
		validateName(name);
		validatePrice(price);
		this.name = name;
		this.price = price;
	}

	public void attachMenuOptionGroup(MenuOptionGroup menuOptionGroup) {
		if (this.menuOptionGroup != null) {
			this.menuOptionGroup.removeMenuOption(this);
		}
		this.menuOptionGroup = menuOptionGroup;
		menuOptionGroup.addMenuOption(this);
	}

	private void validatePrice(int price) {
		if (price < MIN_PRICE) {
			throw new RuntimeException("price는 음수가 되면 안됩니다.");
		}
	}

	private void validateName(String name) {
		if (name == null || name.length() > MAX_NAME_LENGTH) {
			throw new RuntimeException("잘못된 메뉴 옵션 이름 입니다.");
		}
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;

		MenuOption that = (MenuOption)o;

		return menuOptionId.equals(that.menuOptionId);
	}

	@Override
	public int hashCode() {
		return menuOptionId.hashCode();
	}
}
