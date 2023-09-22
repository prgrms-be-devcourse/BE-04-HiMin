package com.prgrms.himin.setup.response;

import java.util.List;

import com.prgrms.himin.member.domain.Permission;
import com.prgrms.himin.member.dto.response.MemberLoginResponse;

public class MemberLoginResponseBuilder {

	public static MemberLoginResponse successBuild() {
		return new MemberLoginResponse(
			"eyJhbGciOiJIUzUxMiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJoaW1pbiIsImlhdCI6MTY5NTI5MzI0MiwiZXhwIjoxNjk1MjkzMzAyLCJ1c2VybmFtZSI6InJucWphYWgxMjM0Iiwicm9sZXMiOlsiUk9MRV9VU0VSIl19.y-EzjHSM7c9rLrdoKrbwYIcVjZT22XgX6RifEyNnVbhkXvB5FQiDIqjHtOx3M1xtv-2r1gt1ChGscVZRjQLgww",
			1L,
			List.of(Permission.ROLE_USER.toString()));
	}
}
