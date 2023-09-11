package com.prgrms.himin.global.error.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

	// Common
	INTERNAL_SERVER_ERROR("COMMON_001", "Internal Server Error"),
	INVALID_REQUEST("COMMON_002", "유효하지 않은 요청입니다."),

	// Member
	MEMBER_NOT_FOUND("MEMBER_001", "회원을 찾을 수 없습니다."),
	MEMBER_BAD_REQUEST("MEMBER_002", "잘못된 회원입니다."),
	MEMBER_LOGIN_FAIL("MEMBER_003", "로그인 정보가 일치하지 않습니다."),
	MEMBER_ADDRESS_NOT_FOUND("MEMBER_004", "주소를 찾을 수 없습니다."),
	MEMBER_ADDRESS_NOT_MATCH("MEMBER_005", "주소가 일치하지 않습니다."),
	MEMBER_ADDRESS_BAD_REQUEST("MEMBER_006", "잘못된 주소 입니다."),
	MEMBER_ADDRESS_ALIAS_BAD_REQUEST("MEMBER_007", "잘못된 주소 가명 입니다."),
	MEMBER_LOGIN_ID_BAD_REQUEST("MEMBER_008", "잘못된 로그인 ID 입니다."),
	MEMBER_PASSWORD_BAD_REQUEST("MEMBER_009", "잘못된 비밀번호 입니다."),
	MEMBER_NAME_BAD_REQUEST("MEMBER_010", "잘못된 회원 이름 입니다."),
	MEMBER_PHONE_BAD_REQUEST("MEMBER_011", "잘못된 회원 핸드폰 번호 입니다."),
	MEMBER_BIRTHDAY_BAD_REQUEST("MEMBER_012", "잘못된 회원 생일입니다."),

	// Menu
	MENU_NOT_FOUND("MENU_001", "메뉴를 찾을 수 없습니다."),
	MENU_BAD_REQUEST("MENU_002", "잘못된 메뉴입니다."),
	MENU_OPTION_NOT_FOUND("MENU_003", "메뉴 옵션을 찾을 수 없습니다."),
	MENU_OPTION_GROUP_NOT_FOUND("MENU_004", "메뉴 옵션 그룹을 찾을 수 없습니다."),
	MENU_OPTION_BAD_REQUEST("MENU_005", "잘못된 메뉴 옵션 입니다."),
	MENU_OPTION_GROUP_BAD_REQUEST("MENU_006", "잘못된 메뉴 옵션 그룹 입니다."),
	MENU_PRICE_BAD_REQUEST("MENU_007", "잘못된 메뉴 가격 입니다."),
	MENU_NAME_BAD_REQUEST("MENU_008", "잘못된 메뉴 이름 입니다."),
	MENU_OPTION_NAME_BAD_REQUEST("MENU_009", "잘못된 메뉴 옵션 이름 입니다."),
	MENU_OPTION_GROUP_NAME_BAD_REQUEST("MENU_010", "잘못된 메뉴 옵션 그룹 이름 입니다."),

	// Order
	ORDER_NOT_FOUND("ORDER_001", "주문을 찾을 수 없습니다."),
	ORDER_BAD_REQUEST("ORDER_002", "잘못된 주문입니다."),
	ORDER_STATUS_BAD_REQUEST("ORDER_003", "잘못된 주문 상태 입니다."),
	ORDER_ADDRESS_BAD_REQUEST("ORDER_004", "잘못된 주문 주소 입니다."),
	ORDER_REQUIREMENT_BAD_REQUEST("ORDER_005", "잘못된 주문 요청사항 입니다."),
	ORDER_ITEM_PRICE_BAD_REQUEST("ORDER_006", "잘못된 주문 아이템 가격 입니다."),
	ORDER_ITEM_QUANTITY_BAD_REQUEST("ORDER_007", "잘못된 주문 아이템 수량 입니다."),
	ORDER_SHOP_NOT_MATCH("ORDER_008", "해당 주문과 가게ID가 일치하지 않습니다."),

	// Shop
	SHOP_NOT_FOUND("SHOP_001", "가게를 찾을 수 없습니다."),
	SHOP_BAD_REQUEST("SHOP_002", "잘못된 가게 입니다."),
	SHOP_NAME_BAD_REQUEST("SHOP_003", "잘못된 가게 이름 입니다."),
	SHOP_ADDRESS_BAD_REQUEST("SHOP_004", "잘못된 가게 주소 입니다."),
	SHOP_PHONE_BAD_REQUEST("SHOP_005", "잘못된 가게 번호입니다."),
	SHOP_DELIVERY_TIP_BAD_REQUEST("SHOP_006", "잘못된 가게 배달팁 입니다."),
	SHOP_OPENING_TIME_BAD_REQUEST("SHOP_007", "잘못된 가게 오픈 시간 입니다."),
	SHOP_CLOSING_TIME_BAD_REQUEST("SHOP_008", "잘못된 가게 마감 시간 입니다."),
	SHOP_SORT_BAD_REQUEST("SHOP_009", "잘못된 가게 정렬 조건 입니다."),

	// Delivery
	DELIVERY_NOT_FOUND("DELIVERY_001", "배달을 찾을 수 없습니다."),
	DELIVERY_BAD_REQUEST("DELIVERY_002", "잘못된 배달입니다."),
	DELIVERY_RIDER_NOT_FOUND("DELIVERY_003", "배달기사를 찾을 수 없습니다."),
	DELIVERY_RIDER_BAD_REQUEST("DELIVERY_004", "잘못된 배달기사 입니다.");

	private final String code;
	private final String message;
}
