package com.prgrms.himin.menu.api;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
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
import com.prgrms.himin.menu.domain.MenuOption;
import com.prgrms.himin.menu.domain.MenuOptionGroup;
import com.prgrms.himin.menu.domain.MenuOptionRepository;
import com.prgrms.himin.menu.dto.request.MenuOptionCreateRequest;
import com.prgrms.himin.menu.dto.request.MenuOptionUpdateRequest;
import com.prgrms.himin.setup.domain.MenuOptionGroupSetUp;
import com.prgrms.himin.setup.domain.MenuOptionSetUp;
import com.prgrms.himin.setup.domain.MenuSetUp;
import com.prgrms.himin.setup.domain.ShopSetUp;
import com.prgrms.himin.setup.request.MenuOptionCreateRequestBuilder;
import com.prgrms.himin.setup.request.MenuOptionUpdateRequestBuilder;
import com.prgrms.himin.shop.domain.Shop;

@Sql("/truncate.sql")
@SpringBootTest
@AutoConfigureMockMvc
public class MenuOptionControllerTest {

	final String BASE_URL = "/api/shops/{shopId}/menus/{menuId}/option-groups/{menuOptionGroupId}/options";

	@Autowired
	MockMvc mvc;

	@Autowired
	ObjectMapper objectMapper;

	@Autowired
	ShopSetUp shopSetUp;

	@Autowired
	MenuSetUp menuSetUp;

	@Autowired
	MenuOptionSetUp menuOptionSetUp;

	@Autowired
	MenuOptionGroupSetUp menuOptionGroupSetUp;

	@Autowired
	MenuOptionRepository menuOptionRepository;

	Shop shop;

	Menu menu;

	MenuOptionGroup menuOptionGroup;

	@BeforeEach
	void eachSetUp() {
		this.shop = shopSetUp.saveOne();
		this.menu = menuSetUp.saveOne(shop);
		this.menuOptionGroup = menuOptionGroupSetUp.saveOne(menu);
	}

	@Nested
	@DisplayName("메뉴 옵션을 생성할 수 있다.")
	class CreateMenuOption {

		private static Stream<Arguments> provideRequestForErrorValue() {
			return Stream.of(
				Arguments.of("마라 칠리 매운 허니버터 와사비 치즈 하바네로 스노윙 볼케이노 청냥 허니 갈릭 마늘 고추바사삭 맛", "메뉴 옵션 이름은 최대 30글자 입니다."),
				Arguments.of(null, "메뉴 옵션 이름이 비어있으면 안됩니다."),
				Arguments.of("", "메뉴 옵션 이름이 비어있으면 안됩니다."),
				Arguments.of("  ", "메뉴 옵션 이름이 비어있으면 안됩니다.")
			);
		}

		@Test
		@DisplayName("성공한다.")
		void success_test() throws Exception {
			// given
			MenuOptionCreateRequest request = MenuOptionCreateRequestBuilder.successBuild();
			String body = objectMapper.writeValueAsString(request);

			// when
			ResultActions resultActions = mvc.perform(post(
					BASE_URL,
					shop.getShopId(),
					menu.getId(),
					menuOptionGroup.getId()
				)
					.content(body)
					.contentType(MediaType.APPLICATION_JSON))
				.andDo(print());

			// then
			resultActions.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("menuOptionId").isNotEmpty())
				.andExpect(jsonPath("name").value(request.name()))
				.andExpect(jsonPath("price").value(request.price()));
		}

		@DisplayName("유효하지 않은 요청값이 들어와서 실패한다.")
		@ParameterizedTest
		@MethodSource("provideRequestForErrorValue")
		void fail_test(String input, String expected) throws Exception {
			// given
			MenuOptionCreateRequest request = MenuOptionCreateRequestBuilder.failBuild(input);
			String body = objectMapper.writeValueAsString(request);

			// when
			ResultActions resultActions = mvc.perform(post(
					BASE_URL,
					shop.getShopId(),
					menu.getId(),
					menuOptionGroup.getId()
				)
					.content(body)
					.contentType(MediaType.APPLICATION_JSON))
				.andDo(print());

			// then
			resultActions.andExpect(status().isBadRequest())
				.andExpect((result) -> assertTrue(
					result.getResolvedException().getClass().isAssignableFrom((MethodArgumentNotValidException.class))))
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("error").value(ErrorCode.INVALID_REQUEST.toString()))
				.andExpect(jsonPath("errors[0].field").value("name"))
				.andExpect(jsonPath("errors[0].value").value(input))
				.andExpect(jsonPath("errors[0].reason").value(expected))
				.andExpect(jsonPath("code").value(ErrorCode.INVALID_REQUEST.getCode()))
				.andExpect(jsonPath("message").value(ErrorCode.INVALID_REQUEST.getMessage()));
		}
	}

	@Nested
	@DisplayName("메뉴 옵션을 수정할 수 있다.")
	class UpdateMenuOption {
		private static Stream<Arguments> provideRequestForErrorValue() {
			return Stream.of(
				Arguments.of("마라 칠리 매운 허니버터 와사비 치즈 하바네로 스노윙 볼케이노 청냥 허니 갈릭 마늘 고추바사삭 맛", "메뉴 옵션 이름은 최대 30글자 입니다."),
				Arguments.of(null, "메뉴 옵션 이름이 비어있으면 안됩니다."),
				Arguments.of("", "메뉴 옵션 이름이 비어있으면 안됩니다."),
				Arguments.of("  ", "메뉴 옵션 이름이 비어있으면 안됩니다.")
			);
		}

		@Test
		@DisplayName("성공한다.")
		void success_test() throws Exception {
			// given
			MenuOption menuOption = menuOptionSetUp.saveOne(menuOptionGroup);

			MenuOptionUpdateRequest request = MenuOptionUpdateRequestBuilder.successBuild();
			String body = objectMapper.writeValueAsString(request);

			// when
			ResultActions resultActions = mvc.perform(post(
					BASE_URL,
					shop.getShopId(),
					menu.getId(),
					menuOptionGroup.getId(),
					menuOption.getId()
				)
					.content(body)
					.contentType(MediaType.APPLICATION_JSON))
				.andDo(print());

			// then
			resultActions.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("menuOptionId").isNotEmpty())
				.andExpect(jsonPath("name").value(request.name()))
				.andExpect(jsonPath("price").value(request.price()));
		}

		@DisplayName("유효하지 않은 요청값이 들어와서 실패한다.")
		@ParameterizedTest
		@MethodSource("provideRequestForErrorValue")
		void fail_test(String input, String expected) throws Exception {
			// given
			MenuOptionUpdateRequest request = MenuOptionUpdateRequestBuilder.failBuild(input);
			String body = objectMapper.writeValueAsString(request);

			// when
			ResultActions resultActions = mvc.perform(post(
					BASE_URL,
					shop.getShopId(),
					menu.getId(),
					menuOptionGroup.getId()
				)
					.content(body)
					.contentType(MediaType.APPLICATION_JSON))
				.andDo(print());

			// then
			resultActions.andExpect(status().isBadRequest())
				.andExpect((result) -> assertTrue(
					result.getResolvedException().getClass().isAssignableFrom((MethodArgumentNotValidException.class))))
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("error").value(ErrorCode.INVALID_REQUEST.toString()))
				.andExpect(jsonPath("errors[0].field").value("name"))
				.andExpect(jsonPath("errors[0].value").value(input))
				.andExpect(jsonPath("errors[0].reason").value(expected))
				.andExpect(jsonPath("code").value(ErrorCode.INVALID_REQUEST.getCode()))
				.andExpect(jsonPath("message").value(ErrorCode.INVALID_REQUEST.getMessage()));
		}
	}

	@Nested
	@DisplayName("메뉴 옵션을 삭제할 수 있다.")
	class DeleteMenuOption {
		final String DELETE_URL = BASE_URL + "/{menuOptionId}";

		@DisplayName("성공한다.")
		@Test
		void success_test() throws Exception {
			// given
			MenuOption savedMenuOption = menuOptionSetUp.saveOne(menuOptionGroup);

			// when
			ResultActions resultActions = mvc.perform(delete(
					DELETE_URL,
					shop.getShopId(),
					menu.getId(),
					menuOptionGroup.getId(),
					savedMenuOption.getId()
				)
					.contentType(MediaType.APPLICATION_JSON))
				.andDo(print());

			// then
			resultActions.andExpect(status().isOk());
			Optional<MenuOption> menuOption = menuOptionRepository.findById(savedMenuOption.getId());
			assertThat(menuOption).isEmpty();
		}

		@DisplayName("유효하지 않은 id로 인해 실패한다.")
		@Test
		void fail_test() throws Exception {
			// given
			long notExistMenuOptionId = 10000;

			// when
			ResultActions resultActions = mvc.perform(delete(
					DELETE_URL,
					shop.getShopId(),
					menu.getId(),
					menuOptionGroup.getId(),
					notExistMenuOptionId
				)
					.contentType(MediaType.APPLICATION_JSON))
				.andDo(print());

			// then
			resultActions.andExpect(status().isNotFound())
				.andExpect((result) -> assertTrue(
					result.getResolvedException().getClass().isAssignableFrom((EntityNotFoundException.class))))
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("error").value(ErrorCode.MENU_OPTION_NOT_FOUND.toString()))
				.andExpect(jsonPath("code").value(ErrorCode.MENU_OPTION_NOT_FOUND.getCode()))
				.andExpect(jsonPath("message").value(ErrorCode.MENU_OPTION_NOT_FOUND.getMessage()));
		}
	}
}
