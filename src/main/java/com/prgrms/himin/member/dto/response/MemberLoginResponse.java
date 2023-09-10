package com.prgrms.himin.member.dto.response;

import java.util.List;

public record MemberLoginResponse(
	String token,
	Long memberId,
	List<String> group
) {
}
