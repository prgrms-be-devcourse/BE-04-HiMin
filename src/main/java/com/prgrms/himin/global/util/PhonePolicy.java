package com.prgrms.himin.global.util;

import java.util.regex.Pattern;

public class PhonePolicy {

	public static final String PHONE_PATTERN = "^(02|0[3-9]{1}[0-9]{1}|010)-[0-9]{3,4}-[0-9]{4}$";

	public static final Pattern PHONE_MATCHER = Pattern.compile(PHONE_PATTERN);

	public static boolean matches(String phone) {
		return PHONE_MATCHER.matcher(phone).matches();
	}
}
