package com.prgrms.himin.order.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.prgrms.himin.menu.domain.MenuOption;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "selected_options")
public class SelectedOption {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long selectedOptionId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "order_item_id", nullable = false)
	private OrderItem orderItem;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "menu_option_id", nullable = false)
	private MenuOption menuOption;

	private SelectedOption(
		MenuOption menuOption
	) {
		validateMenuOption(menuOption);
		this.menuOption = menuOption;
	}

	public void attachTo(OrderItem orderItem) {
		if (this.orderItem != null) {
			orderItem.removeSelectedOption(this);
		}
		this.orderItem = orderItem;
		orderItem.addSelectedOption(this);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass())
			return false;
		SelectedOption that = (SelectedOption)o;
		return Objects.equals(menuOption.getId(), that.menuOption.getId());
	}

	@Override
	public int hashCode() {
		return Objects.hash(
			selectedOptionId,
			orderItem,
			menuOption
		);
	}

	public static List<SelectedOption> from(List<MenuOption> menuOptions) {
		List<SelectedOption> selectedOptions = new ArrayList<>();
		for (MenuOption menuOption : menuOptions) {
			SelectedOption selectedOption = new SelectedOption(menuOption);
			selectedOptions.add(selectedOption);
		}
		return selectedOptions;
	}

	private void validateMenuOption(MenuOption menuOption) {
		if (menuOption == null) {
			throw new RuntimeException("menuOption이 null일 수 없습니다.");
		}
	}
}
