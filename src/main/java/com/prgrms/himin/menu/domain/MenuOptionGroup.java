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
import lombok.Builder;
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

	@Column(name = "menu_group_name")
	private String name;

	@Column(name = "menu_id", insertable = false, updatable = false)
	private Long menuId;

	@ManyToOne
	@JoinColumn(name = "menu_id")
	private Menu menu;

	@Builder
	public MenuOptionGroup(
		String name,
		Long menuId
	) {
		validateName(name);
		this.name = name;
		this.menuId = menuId;
	}

	private void validateName(String name) {
		if (name == null || name.length() > MAX_NAME_LENGTH) {
			throw new RuntimeException("잘못된 메뉴 옵션 그룹 이름 입니다.");
		}
	}
}
