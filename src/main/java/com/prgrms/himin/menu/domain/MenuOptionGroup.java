package com.prgrms.himin.menu.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
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
@Table(name = "menu_option_group")
public class MenuOptionGroup {

	private static final int MAX_NAME_LENGTH = 30;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;

	@Column(name = "menu_group_name", nullable = false, length = 30)
	private String name;

	@ManyToOne
	@JoinColumn(name = "menu_id")
	private Menu menu;

	public MenuOptionGroup(
		String name
	) {
		validateName(name);
		this.name = name;
	}

	public void attachMenu(Menu menu) {
		this.menu = menu;
	}

	private void validateName(String name) {
		if (name == null || name.length() > MAX_NAME_LENGTH) {
			throw new RuntimeException("잘못된 메뉴 옵션 그룹 이름 입니다.");
		}
	}
}
