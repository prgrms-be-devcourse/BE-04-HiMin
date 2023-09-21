package com.prgrms.himin.rider.docs;

import static org.mockito.BDDMockito.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.prgrms.himin.delivery.api.RiderController;
import com.prgrms.himin.delivery.application.RiderService;
import com.prgrms.himin.delivery.dto.request.RiderCreateRequest;
import com.prgrms.himin.delivery.dto.response.RiderResponse;
import com.prgrms.himin.setup.request.RiderCreateRequestBuilder;
import com.prgrms.himin.setup.response.RiderResponseBuilder;

@AutoConfigureRestDocs
@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(RiderController.class)
public class RiderDocumentationTest {

	@Autowired
	private MockMvc mvc;

	@Autowired
	private ObjectMapper objectMapper;

	@MockBean
	private RiderService riderService;

	@DisplayName("배달기사를 생성할 수 있다.")
	@Test
	void createRider() throws Exception {
		// given
		RiderCreateRequest request = RiderCreateRequestBuilder.successBuild();
		RiderResponse response = RiderResponseBuilder.successBuild();

		given(riderService.createRider(any(RiderCreateRequest.class))).willReturn(response);

		// when
		ResultActions resultAction = mvc.perform(post("/api/riders")
			.accept(MediaType.APPLICATION_JSON)
			.contentType(MediaType.APPLICATION_JSON)
			.content(objectMapper.writeValueAsString(request)));

		// then
		resultAction.andExpect(status().isOk())
			.andDo(document("rider-create",
				preprocessRequest(prettyPrint()),
				preprocessResponse(prettyPrint()),
				requestFields(
					fieldWithPath("name").type(JsonFieldType.STRING).description("배달기사 이름"),
					fieldWithPath("phone").type(JsonFieldType.STRING).description("배달기사 전화번호")
				),
				responseFields(
					fieldWithPath("riderId").type(JsonFieldType.NUMBER).description("ID"),
					fieldWithPath("name").type(JsonFieldType.STRING).description("이름"),
					fieldWithPath("phone").type(JsonFieldType.STRING).description("전화번호")
				)));
	}

}
