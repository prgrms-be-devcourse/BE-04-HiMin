package com.prgrms.himin.menu.api;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.web.bind.MethodArgumentNotValidException;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.prgrms.himin.global.error.exception.ErrorCode;
import com.prgrms.himin.menu.domain.Menu;
import com.prgrms.himin.menu.domain.MenuRepository;
import com.prgrms.himin.menu.dto.request.MenuCreateRequest;
import com.prgrms.himin.menu.dto.request.MenuOptionGroupCreateRequest;
import com.prgrms.himin.setup.domain.MenuOptionGroupSetUp;
import com.prgrms.himin.setup.domain.MenuSetUp;
import com.prgrms.himin.setup.domain.ShopSetUp;
import com.prgrms.himin.setup.request.MenuCreateRequestBuilder;
import com.prgrms.himin.setup.request.MenuOptionGroupRequestBuilder;
import com.prgrms.himin.shop.domain.Shop;

@SpringBootTest
@AutoConfigureMockMvc
public class MenuOptionGroupControllerTest {

	final String BASE_URL = "/api/shops/{shopId}/menus/{menuId}/option-groups";

	@Autowired
	MenuRepository menuRepository;

	@Autowired
	MockMvc mvc;

	@Autowired
	ObjectMapper objectMapper;

	@Autowired
	ShopSetUp shopSetUp;

	@Autowired
	MenuSetUp menuSetUp;

	@Autowired
	MenuOptionGroupSetUp menuOptionGroupSetUp;

	Shop shop;

	Menu menu;

	@BeforeEach
	void eachSetUp() {
		this.shop = shopSetUp.saveOne();
		this.menu = menuSetUp.saveOne(shop);
	}

	@Nested
	@DisplayName("메뉴 옵션 그룹 생성을 할 수 있다.")
	class CreateMenuOptionGroup {

		private static Stream<Arguments> provideRequestForErrorValue() {
			return Stream.of(
				Arguments.of("마라 칠리 매운 허니버터 와사비 치즈 하바네로 맛 150도 오븐에서 30분이 상 구운 치킨 오븐구이", "메뉴 옵션 그룹 이름은 최대 30글자 입니다."),
				Arguments.of(null, "메뉴 옵션 그룹 이름은 비어있으면 안됩니다."),
				Arguments.of("", "메뉴 옵션 그룹 이름은 비어있으면 안됩니다."),
				Arguments.of("  ", "메뉴 옵션 그룹 이름은 비어있으면 안됩니다.")
			);
		}

		@Test
		@DisplayName("성공한다.")
		void success_test() throws Exception {
			// given
			MenuOptionGroupCreateRequest request = MenuOptionGroupRequestBuilder.successBuild();
			String body = objectMapper.writeValueAsString(request);

			// when
			ResultActions resultActions = mvc.perform(post(
					BASE_URL,
					shop.getShopId(),
					menu.getId()
				)
					.content(body)
					.contentType(MediaType.APPLICATION_JSON))
				.andDo(print());

			// then
			resultActions.andExpect(status().isOk())
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("menuOptionGroupId").isNotEmpty())
				.andExpect(jsonPath("name").value(request.name()));

		}

		@ParameterizedTest
		@MethodSource("provideRequestForErrorValue")
		@DisplayName("실패한다.")
		void fail_test(String input, String expected) throws Exception {
			// given
			MenuCreateRequest request = MenuCreateRequestBuilder.failBuild(input);
			String body = objectMapper.writeValueAsString(request);

			// when
			ResultActions resultActions = mvc.perform(post(
					BASE_URL,
					shop.getShopId(),
					menu.getId()
				)
					.content(body)
					.contentType(MediaType.APPLICATION_JSON))
				.andDo(print());

			// then
			resultActions.andExpect(status().isBadRequest())
				.andExpect((result) -> assertTrue(
					result.getResolvedException().getClass().isAssignableFrom((MethodArgumentNotValidException.class))))
				.andExpect(jsonPath("error").value(ErrorCode.INVALID_REQUEST.toString()))
				.andExpect(jsonPath("errors[0].field").value("name"))
				.andExpect(jsonPath("errors[0].value").value(input))
				.andExpect(jsonPath("errors[0].reason").value(expected))
				.andExpect(jsonPath("code").value(ErrorCode.INVALID_REQUEST.getCode()))
				.andExpect(jsonPath("message").value(ErrorCode.INVALID_REQUEST.getMessage()));
		}
	}
}
