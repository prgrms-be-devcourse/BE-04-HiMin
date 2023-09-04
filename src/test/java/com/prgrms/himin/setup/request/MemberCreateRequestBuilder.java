package com.prgrms.himin.setup.request;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import com.prgrms.himin.member.dto.request.MemberCreateRequest;

public class MemberCreateRequestBuilder {

	public static MemberCreateRequest successBuild() {
		return new MemberCreateRequest(
			"rnqjaah1234",
			"1234",
			"구범모",
			"010-4691-3526",
			LocalDate.parse("1998-08-12", DateTimeFormatter.ISO_DATE),
			"사근동 217-5",
			"우리집"
		);
	}

	public static MemberCreateRequest failBuild(String loginId) {
		return new MemberCreateRequest(
			loginId,
			"1234",
			"구범모",
			"010-4691-3526",
			LocalDate.parse("1998-08-12", DateTimeFormatter.ISO_DATE),
			"사근동 217-5",
			"우리집"
		);
	}
}
