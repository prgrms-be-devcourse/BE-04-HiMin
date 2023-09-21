package com.prgrms.himin.setup.response;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import com.prgrms.himin.member.domain.Grade;
import com.prgrms.himin.member.dto.response.AddressResponse;
import com.prgrms.himin.member.dto.response.MemberCreateResponse;
import com.prgrms.himin.member.dto.response.MemberResponse;

public class MemberResponseBuilder {

	public static MemberCreateResponse successCreateBuild() {
		return new MemberCreateResponse(
			1L,
			"rnqjaah1234",
			"구범모",
			"010-4691-3526",
			LocalDate.parse("1998-08-12", DateTimeFormatter.ISO_DATE),
			Grade.NEW,
			List.of(
				new AddressResponse(
					1L,
					"우리집",
					"사근동 217-5")
			)
		);
	}

	public static MemberResponse successGetBuild() {
		return new MemberResponse(
			1L,
			"rnqjaah1234",
			"구범모",
			"010-4691-3526",
			LocalDate.parse("1998-08-12", DateTimeFormatter.ISO_DATE),
			Grade.NEW,
			List.of(
				new AddressResponse(
					1L,
					"우리집",
					"사근동 217-5")
			)
		);
	}
}
