package com.prgrms.himin.menu.api;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Optional;
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
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.web.bind.MethodArgumentNotValidException;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.prgrms.himin.global.error.exception.EntityNotFoundException;
import com.prgrms.himin.global.error.exception.ErrorCode;
import com.prgrms.himin.menu.domain.Menu;
import com.prgrms.himin.menu.domain.MenuRepository;
import com.prgrms.himin.menu.dto.request.MenuCreateRequest;
import com.prgrms.himin.menu.dto.request.MenuUpdateRequest;
import com.prgrms.himin.setup.domain.MenuSetUp;
import com.prgrms.himin.setup.domain.ShopSetUp;
import com.prgrms.himin.setup.request.MenuCreateRequestBuilder;
import com.prgrms.himin.setup.request.MenuUpdateRequestBuilder;
import com.prgrms.himin.shop.domain.Shop;

@Sql("/truncate.sql")
@SpringBootTest
@AutoConfigureMockMvc
class MenuControllerTest {

	final String BASE_URL = "/api/shops/{shopId}/menus";

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

	Shop shop;

	@BeforeEach
	void eachSetUp() {
		this.shop = shopSetUp.saveOne();
	}

	@Nested
	@DisplayName("메뉴 생성을 할 수 있다.")
	class CreateMenu {

		private static Stream<Arguments> provideRequestForErrorValue() {
			return Stream.of(
				Arguments.of("마라 칠리 매운 허니버터 와사비 치즈 하바네로 맛 150도 오븐에서 30분이 상 구운 치킨 오븐구이", "메뉴 이름은 최대 30글자 입니다."),
				Arguments.of(null, "메뉴 이름이 비어있으면 안됩니다."),
				Arguments.of("", "메뉴 이름이 비어있으면 안됩니다."),
				Arguments.of("  ", "메뉴 이름이 비어있으면 안됩니다.")
			);
		}

		@DisplayName("성공한다.")
		@Test
		void success_test() throws Exception {
			// given
			MenuCreateRequest request = MenuCreateRequestBuilder.successBuild();
			String body = objectMapper.writeValueAsString(request);

			// when
			ResultActions resultActions = mvc.perform(post(BASE_URL, shop.getShopId())
				.content(body)
				.contentType(MediaType.APPLICATION_JSON));

			// then
			resultActions.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("menuId").isNotEmpty())
				.andExpect(jsonPath("name").value(request.name()))
				.andExpect(jsonPath("price").value(request.price()))
				.andExpect(jsonPath("popularity").value(request.popularity()));
		}

		@DisplayName("실패한다.")
		@ParameterizedTest
		@MethodSource("provideRequestForErrorValue")
		void fail_test(String input, String expected) throws Exception {
			// given
			MenuCreateRequest request = MenuCreateRequestBuilder.failBuild(input);
			String body = objectMapper.writeValueAsString(request);

			// when
			ResultActions resultActions = mvc.perform(post(BASE_URL, shop.getShopId())
				.content(body)
				.contentType(MediaType.APPLICATION_JSON));

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

	@Nested
	@DisplayName("메뉴 조회를 할 수 있다.")
	class GetMenu {

		final String GET_URL = BASE_URL + "/{menuId}";

		@DisplayName("성공한다.")
		@Test
		void success_test() throws Exception {
			// given
			Menu savedMenu = menuSetUp.saveOne(shop);

			// when
			ResultActions resultActions = mvc.perform(get(
				GET_URL,
				shop.getShopId(),
				savedMenu.getId()
			)
				.contentType(MediaType.APPLICATION_JSON));

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
			int notExistMenuId = 10000;

			// when
			ResultActions resultActions = mvc.perform(get(
				GET_URL,
				shop.getShopId(),
				notExistMenuId
			)
				.contentType(MediaType.APPLICATION_JSON));

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

		final String UPDATE_URL = BASE_URL + "/{menuId}";

		private static Stream<Arguments> provideRequestForErrorValue() {
			return Stream.of(
				Arguments.of("마라 칠리 매운 허니버터 와사비 치즈 하바네로 맛 150도 오븐에서 30분이 상 구운 치킨 오븐구이", "메뉴 이름은 최대 30글자 입니다."),
				Arguments.of(null, "메뉴 이름이 비어있으면 안됩니다."),
				Arguments.of("", "메뉴 이름이 비어있으면 안됩니다."),
				Arguments.of("  ", "메뉴 이름이 비어있으면 안됩니다.")
			);
		}

		@DisplayName("성공한다.")
		@Test
		void success_test() throws Exception {
			// given
			Menu savedMenu = menuSetUp.saveOne(shop);

			MenuUpdateRequest.Info request = MenuUpdateRequestBuilder.infoSuccessBuild();
			String body = objectMapper.writeValueAsString(request);

			// when
			ResultActions resultActions = mvc.perform(put(
				UPDATE_URL,
				shop.getShopId(),
				savedMenu.getId()
			)
				.content(body)
				.contentType(MediaType.APPLICATION_JSON));

			// then
			resultActions.andExpect(status().isNoContent());
			Optional<Menu> maySavedMenu = menuRepository.findById(savedMenu.getId());
			assertThat(maySavedMenu.isPresent()).isTrue();
			savedMenu = maySavedMenu.get();
			assertThat(savedMenu.getName()).isEqualTo(request.name());
		}

		@DisplayName("실패한다.")
		@ParameterizedTest
		@MethodSource("provideRequestForErrorValue")
		void fail_test(String input, String expected) throws Exception {
			// given
			Menu savedMenu = menuSetUp.saveOne(shop);

			MenuUpdateRequest.Info request = MenuUpdateRequestBuilder.infoFailBuild(input);
			String body = objectMapper.writeValueAsString(request);

			// when
			ResultActions resultActions = mvc.perform(put(
				UPDATE_URL,
				shop.getShopId(),
				savedMenu.getId()
			)
				.content(body)
				.contentType(MediaType.APPLICATION_JSON));

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

	@Nested
	@DisplayName("메뉴 삭제를 할 수 있다.")
	class DeleteMenu {

		final String DELETE_URL = BASE_URL + "/{menuId}";

		@DisplayName("성공한다.")
		@Test
		void success_test() throws Exception {
			// given
			Menu savedMenu = menuSetUp.saveOne(shop);

			// when
			ResultActions resultActions = mvc.perform(delete(
				DELETE_URL,
				shop.getShopId(),
				savedMenu.getId()
			));

			// then
			resultActions.andExpect(status().isOk());
			Optional<Menu> menu = menuRepository.findById(savedMenu.getId());
			assertThat(menu).isEmpty();
		}

		@DisplayName("실패한다.")
		@Test
		void fail_test() throws Exception {
			// given
			int notExistMenuId = 10000;

			// when
			ResultActions resultActions = mvc.perform(delete(
				DELETE_URL,
				shop.getShopId(),
				notExistMenuId
			));

			// then
			resultActions.andExpect(status().isNotFound())
				.andExpect((result) -> assertTrue(
					result.getResolvedException().getClass().isAssignableFrom((EntityNotFoundException.class))))
				.andExpect(jsonPath("error").value(ErrorCode.MENU_NOT_FOUND.toString()))
				.andExpect(jsonPath("code").value(ErrorCode.MENU_NOT_FOUND.getCode()))
				.andExpect(jsonPath("message").value(ErrorCode.MENU_NOT_FOUND.getMessage()));
		}
	}
}
