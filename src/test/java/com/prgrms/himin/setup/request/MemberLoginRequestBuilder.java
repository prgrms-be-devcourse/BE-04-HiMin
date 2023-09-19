package com.prgrms.himin.setup.request;

import com.prgrms.himin.member.dto.request.MemberLoginRequest;

public class MemberLoginRequestBuilder {

	public static MemberLoginRequest successBuild() {
		return new MemberLoginRequest("rnqjaah1234", "1234");
	}

	public static MemberLoginRequest failBuild(String loginId, String password) {
		return new MemberLoginRequest(loginId, password);
	}
}
