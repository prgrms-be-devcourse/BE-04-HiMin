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
import com.prgrms.himin.menu.domain.MenuOptionGroup;
import com.prgrms.himin.menu.domain.MenuOptionGroupRepository;
import com.prgrms.himin.menu.domain.MenuRepository;
import com.prgrms.himin.menu.dto.request.MenuCreateRequest;
import com.prgrms.himin.menu.dto.request.MenuOptionGroupCreateRequest;
import com.prgrms.himin.menu.dto.request.MenuOptionGroupUpdateRequest;
import com.prgrms.himin.setup.domain.MenuOptionGroupSetUp;
import com.prgrms.himin.setup.domain.MenuSetUp;
import com.prgrms.himin.setup.domain.ShopSetUp;
import com.prgrms.himin.setup.request.MenuCreateRequestBuilder;
import com.prgrms.himin.setup.request.MenuOptionGroupRequestBuilder;
import com.prgrms.himin.setup.request.MenuOptionGroupUpdateRequestBuilder;
import com.prgrms.himin.shop.domain.Shop;

@Sql("/truncate.sql")
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

	@Autowired
	MenuOptionGroupRepository menuOptionGroupRepository;

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
				Arguments.of("마라 칠리 매운 허니버터 와사비 치즈 하바네로 맛 150도 오븐에서 30분이상 구운 치킨 오븐구이 주방장 선택",
					"메뉴 옵션 그룹 이름은 최대 30글자 입니다."),
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
				.contentType(MediaType.APPLICATION_JSON));

			// then
			resultActions.andExpect(status().isOk())
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
	@DisplayName("메뉴 옵션 그룹 수정을 할 수 있다.")
	class UpdateMenuOptionGroup {

		final String UPDATE_URL = BASE_URL + "/{menuOptionGroupId}";

		private static Stream<Arguments> provideRequestForErrorValue() {
			return Stream.of(
				Arguments.of("마라 칠리 매운 허니버터 와사비 치즈 하바네로 맛 150도 오븐에서 30분이상 구운 치킨 오븐구이 주방장 선택",
					"메뉴 옵션 그룹 이름은 최대 30글자 입니다."),
				Arguments.of(null, "메뉴 옵션 그룹 이름은 비어있으면 안됩니다."),
				Arguments.of("", "메뉴 옵션 그룹 이름은 비어있으면 안됩니다."),
				Arguments.of("  ", "메뉴 옵션 그룹 이름은 비어있으면 안됩니다.")
			);
		}

		@Test
		@DisplayName("성공한다.")
		void success_test() throws Exception {
			// given
			MenuOptionGroup savedMenuOptionGroup = menuOptionGroupSetUp.saveOne(menu);

			MenuOptionGroupUpdateRequest request = MenuOptionGroupUpdateRequestBuilder.successBuild();
			String body = objectMapper.writeValueAsString(request);

			// when
			ResultActions resultActions = mvc.perform(put(
				UPDATE_URL,
				shop.getShopId(),
				menu.getId(),
				savedMenuOptionGroup.getId()
			)
				.content(body)
				.contentType(MediaType.APPLICATION_JSON));

			// then
			resultActions.andExpect(status().isNoContent());
			Optional<MenuOptionGroup> maySavedMenuOptionGroup = menuOptionGroupRepository
				.findById(savedMenuOptionGroup.getId());
			assertThat(maySavedMenuOptionGroup.isPresent()).isTrue();
			savedMenuOptionGroup = maySavedMenuOptionGroup.get();
			assertThat(savedMenuOptionGroup.getName()).isEqualTo(request.name());
		}

		@ParameterizedTest
		@MethodSource("provideRequestForErrorValue")
		@DisplayName("실패한다.")
		void fail_test(String input, String expected) throws Exception {
			// given
			MenuOptionGroup savedMenuOptionGroup = menuOptionGroupSetUp.saveOne(menu);

			MenuOptionGroupUpdateRequest request = MenuOptionGroupUpdateRequestBuilder.failBuild(input);
			String body = objectMapper.writeValueAsString(request);

			// when
			ResultActions resultActions = mvc.perform(put(
				UPDATE_URL,
				shop.getShopId(),
				menu.getId(),
				savedMenuOptionGroup.getId()
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
	@DisplayName("메뉴 옵션 그룹을 삭제할 수 있다.")
	class DeleteMenuOptionGroup {

		final String DELETE_URL = BASE_URL + "/{menuOptionGroupId}";

		@Test
		@DisplayName("성공한다.")
		void success_test() throws Exception {
			// given
			MenuOptionGroup savedMenuOptionGroup = menuOptionGroupSetUp.saveOne(menu);

			// when
			ResultActions resultActions = mvc.perform(delete(
				DELETE_URL,
				shop.getShopId(),
				menu.getId(),
				savedMenuOptionGroup.getId()
			));

			// then
			resultActions.andExpect(status().isOk());
			Optional<MenuOptionGroup> menuOptionGroup = menuOptionGroupRepository
				.findById(savedMenuOptionGroup.getId());
			assertThat(menuOptionGroup).isEmpty();
		}

		@DisplayName("실패한다.")
		@Test
		void fail_test() throws Exception {
			// given
			int notExistMenuOptionGroupId = 10000;

			// when
			ResultActions resultActions = mvc.perform(delete(
				DELETE_URL,
				shop.getShopId(),
				menu.getId(),
				notExistMenuOptionGroupId
			));

			// then
			resultActions.andExpect(status().isNotFound())
				.andExpect((result) -> assertTrue(
					result.getResolvedException().getClass().isAssignableFrom((EntityNotFoundException.class))))
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("error").value(ErrorCode.MENU_OPTION_GROUP_NOT_FOUND.toString()))
				.andExpect(jsonPath("code").value(ErrorCode.MENU_OPTION_GROUP_NOT_FOUND.getCode()))
				.andExpect(jsonPath("message").value(ErrorCode.MENU_OPTION_GROUP_NOT_FOUND.getMessage()));
		}
	}
}
