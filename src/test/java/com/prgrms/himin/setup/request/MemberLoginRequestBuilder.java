package com.prgrms.himin.setup.request;

import com.prgrms.himin.member.dto.request.MemberLoginRequest;

public class MemberLoginRequestBuilder {

	public static MemberLoginRequest successBuild(String loginId, String password) {
		return new MemberLoginRequest(
			loginId,
			password
		);
	}

	public static MemberLoginRequest failBuild(String loginId, String password) {
		return new MemberLoginRequest(
			loginId,
			password
		);
	}
}
