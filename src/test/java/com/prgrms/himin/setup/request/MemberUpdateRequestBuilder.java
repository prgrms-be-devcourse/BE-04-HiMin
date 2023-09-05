package com.prgrms.himin.setup.request;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import com.prgrms.himin.member.dto.request.MemberUpdateRequest;

public class MemberUpdateRequestBuilder {

	public static MemberUpdateRequest.Info infoSuccessBuild() {
		return new MemberUpdateRequest.Info(
			"rnqjaah1234",
			"1234",
			"업데이트구범모",
			"010-4691-3526",
			LocalDate.parse("1998-08-12", DateTimeFormatter.ISO_DATE)
		);
	}

	public static MemberUpdateRequest.Info infoFailBuild(String loginId) {
		return new MemberUpdateRequest.Info(
			loginId,
			"1234",
			"업데이트구범모",
			"010-4691-3526",
			LocalDate.parse("1998-08-12", DateTimeFormatter.ISO_DATE)
		);
	}

}
