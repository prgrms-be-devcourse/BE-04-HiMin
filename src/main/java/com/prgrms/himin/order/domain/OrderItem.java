package com.prgrms.himin.order.domain;

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

import com.prgrms.himin.global.error.exception.BusinessException;
import com.prgrms.himin.global.error.exception.ErrorCode;
import com.prgrms.himin.menu.domain.Menu;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "order_items")
public class OrderItem {

	public static final int MIN_ORDER_QUANTITY = 1;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long orderItemId;

	@Column(name = "quantity")
	private int quantity;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "order_id", nullable = false)
	private Order order;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "menu_id", nullable = false)
	private Menu menu;

	@OneToMany(mappedBy = "orderItem", cascade = CascadeType.ALL)
	private List<SelectedOption> selectedOptions = new ArrayList<>();

	public OrderItem(
		Menu menu,
		int quantity
	) {
		validateMenu(menu);
		this.quantity = quantity;
		this.menu = menu;
	}

	public void attachTo(Order order) {
		if (this.order != null) {
			order.removeOrderItem(this);
		}
		order.addOrderItem(this);
		this.order = order;
	}

	public void addSelectedOption(SelectedOption selectedOption) {
		if (selectedOptions.contains(selectedOption)) {
			return;
		}
		selectedOptions.add(selectedOption);
	}

	public void removeSelectedOption(SelectedOption selectedOption) {
		selectedOptions.remove(selectedOption);
	}

	public int calculateOrderItemPrice() {
		int result = 0;
		for (SelectedOption selectedOption : selectedOptions) {
			result += selectedOption.getMenuOption().getPrice() * quantity;
		}
		result += menu.getPrice() * quantity;

		return result;
	}

	private void validateMenu(Menu menu) {
		if (menu == null) {
			throw new BusinessException(ErrorCode.MENU_BAD_REQUEST);
		}
	}
}
