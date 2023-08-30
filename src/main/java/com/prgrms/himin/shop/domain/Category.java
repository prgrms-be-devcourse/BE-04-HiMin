package com.prgrms.himin.shop.domain;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Category {
    FAST_FOOD("패스트푸드"),
    CAFE("카페"),
    ASIAN("아시안"),
    CHINESE("중식"),
    JAPANESE("일식"),
    WESTERN("양식"),
    PIZZA("피자");

    private final String name;
}
