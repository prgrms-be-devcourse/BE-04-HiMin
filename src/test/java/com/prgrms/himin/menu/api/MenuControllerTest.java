package com.prgrms.himin.menu.api;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.web.bind.MethodArgumentNotValidException;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.prgrms.himin.global.error.exception.EntityNotFoundException;
import com.prgrms.himin.global.error.exception.ErrorCode;
import com.prgrms.himin.menu.domain.Menu;
import com.prgrms.himin.menu.dto.request.MenuCreateRequest;
import com.prgrms.himin.menu.dto.request.MenuUpdateRequest;
import com.prgrms.himin.setup.domain.MenuSetUp;
import com.prgrms.himin.setup.domain.ShopSetUp;
import com.prgrms.himin.setup.request.MenuCreateRequestBuilder;
import com.prgrms.himin.setup.request.MenuUpdateRequestBuilder;
import com.prgrms.himin.shop.api.ShopController;
import com.prgrms.himin.shop.application.ShopService;
import com.prgrms.himin.shop.domain.Shop;

@SpringBootTest
@AutoConfigureMockMvc
class MenuControllerTest {

	@Autowired
	ShopController shopController;

	@Autowired
	ShopService shopService;

	@Autowired
	MockMvc mvc;

	@Autowired
	ObjectMapper objectMapper;

	@Autowired
	ShopSetUp shopSetUp;

	@Autowired
	MenuSetUp menuSetUp;

	String baseURL;

	Shop shop;

	@BeforeEach
	void eachSetUp() {
		this.shop = shopSetUp.saveOne();
		baseURL = "/api/shops/" + shop.getShopId() + "/menus";
	}

	@Nested
	@DisplayName("메뉴 생성을 할 수 있다.")
	class CreateMenu {

		@DisplayName("성공한다.")
		@Test
		void success_test() throws Exception {
			// given
			MenuCreateRequest request = MenuCreateRequestBuilder.successBuild();
			String body = objectMapper.writeValueAsString(request);

			// when
			ResultActions resultActions = mvc.perform(post(baseURL)
					.content(body)
					.contentType(MediaType.APPLICATION_JSON))
				.andDo(print());

			// then
			resultActions.andExpect(status().isOk())
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("menuId").isNotEmpty())
				.andExpect(jsonPath("name").value(request.name()))
				.andExpect(jsonPath("price").value(request.price()))
				.andExpect(jsonPath("popularity").value(request.popularity()));
		}

		@DisplayName("실패한다.")
		@ParameterizedTest
		@ValueSource(strings = {"", " "})
		void fail_test(String input) throws Exception {
			// given
			MenuCreateRequest request = MenuCreateRequestBuilder.failBuild(input);
			String body = objectMapper.writeValueAsString(request);

			// when
			ResultActions resultActions = mvc.perform(post(baseURL)
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
				.andExpect(jsonPath("errors[0].reason").value("메뉴 이름이 비어있으면 안됩니다."))
				.andExpect(jsonPath("code").value(ErrorCode.INVALID_REQUEST.getCode()))
				.andExpect(jsonPath("message").value(ErrorCode.INVALID_REQUEST.getMessage()));
		}
	}

	@Nested
	@DisplayName("메뉴 조회를 할 수 있다.")
	class GetMenu {

		@DisplayName("성공한다.")
		@Test
		void success_test() throws Exception {
			// given
			Menu savedMenu = menuSetUp.saveOne(shop);
			final String GET_URL = "%s/%d".formatted(
				baseURL,
				savedMenu.getId()
			);

			// when
			ResultActions resultActions = mvc.perform(get(GET_URL)
					.contentType(MediaType.APPLICATION_JSON))
				.andDo(print());

			// then
			resultActions.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("menuId").isNotEmpty())
				.andExpect(jsonPath("name").value(savedMenu.getName()))
				.andExpect(jsonPath("price").value(savedMenu.getPrice()))
				.andExpect(jsonPath("popularity").value(savedMenu.isPopularity()));
		}

		@DisplayName("실패한다.")
		@Test
		void fail_test() throws Exception {
			// given
			int notExistId = 10000;
			final String FAIL_GET_URL = "%s/%d".formatted(
				baseURL,
				notExistId
			);

			// when
			ResultActions resultActions = mvc.perform(get(FAIL_GET_URL)
					.contentType(MediaType.APPLICATION_JSON))
				.andDo(print());

			// then
			resultActions.andExpect(status().isNotFound())
				.andExpect((result) -> assertTrue(
					result.getResolvedException().getClass().isAssignableFrom((EntityNotFoundException.class))))
				.andExpect(jsonPath("error").value(ErrorCode.MENU_NOT_FOUND.toString()))
				.andExpect(jsonPath("code").value(ErrorCode.MENU_NOT_FOUND.getCode()))
				.andExpect(jsonPath("message").value(ErrorCode.MENU_NOT_FOUND.getMessage()));
		}
	}

	@Nested
	@DisplayName("메뉴 수정을 할 수 있다.")
	class UpdateMenu {

		@DisplayName("성공한다.")
		@Test
		void success_test() throws Exception {
			// given
			Menu savedMenu = menuSetUp.saveOne(shop);
			final String UPDATE_URL = "%s/%d".formatted(
				baseURL,
				savedMenu.getId()
			);
			MenuUpdateRequest.Info request = MenuUpdateRequestBuilder.successBuild();
			String body = objectMapper.writeValueAsString(request);

			// when
			ResultActions resultActions = mvc.perform(put(UPDATE_URL)
					.content(body)
					.contentType(MediaType.APPLICATION_JSON))
				.andDo(print());

			// then
			resultActions.andExpect(status().isNoContent());
		}

		@DisplayName("실패한다.")
		@ParameterizedTest
		@ValueSource(strings = {"", " "})
		void fail_test(String input) throws Exception {
			// given
			Menu savedMenu = menuSetUp.saveOne(shop);
			final String UPDATE_URL = "%s/%d".formatted(
				baseURL,
				savedMenu.getId()
			);
			MenuUpdateRequest.Info request = MenuUpdateRequestBuilder.failBuild(input);
			String body = objectMapper.writeValueAsString(request);

			// when
			ResultActions resultActions = mvc.perform(put(UPDATE_URL)
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
				.andExpect(jsonPath("errors[0].reason").value("메뉴 이름이 비어있으면 안됩니다."))
				.andExpect(jsonPath("code").value(ErrorCode.INVALID_REQUEST.getCode()))
				.andExpect(jsonPath("message").value(ErrorCode.INVALID_REQUEST.getMessage()));
		}
	}

	@Nested
	@DisplayName("메뉴 삭제를 할 수 있다.")
	class DeleteMenu {

		@DisplayName("성공한다.")
		@Test
		void success_test() throws Exception {
			// given
			Menu savedMenu = menuSetUp.saveOne(shop);
			final String DELETE_URL = "%s/%d".formatted(
				baseURL,
				savedMenu.getId()
			);

			// when
			ResultActions resultActions = mvc.perform(delete(DELETE_URL)
					.contentType(MediaType.APPLICATION_JSON))
				.andDo(print());

			// then
			resultActions.andExpect(status().isOk());
		}

		@DisplayName("실패한다.")
		@Test
		void fail_test() throws Exception {
			// given
			int notExistId = 10000;
			final String DELETE_URL = "%s/%d".formatted(
				baseURL,
				notExistId
			);

			// when
			ResultActions resultActions = mvc.perform(delete(DELETE_URL)
					.contentType(MediaType.APPLICATION_JSON))
				.andDo(print());

			// then
			resultActions.andExpect(status().isNotFound())
				.andExpect((result) -> assertTrue(
					result.getResolvedException().getClass().isAssignableFrom((EntityNotFoundException.class))));
		}
	}
}
