package com.prgrms.himin.global.util;

import java.util.regex.Pattern;

public class PhonePolicy {

	private static final Pattern PHONE_PATTERN = Pattern.compile("^(02|0[3-9]{1}[0-9]{1}|010)-[0-9]{3,4}-[0-9]{4}$");

	public static boolean matches(String phone) {
		return PHONE_PATTERN.matcher(phone).matches();
	}
}
