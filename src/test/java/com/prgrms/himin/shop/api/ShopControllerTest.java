package com.prgrms.himin.shop.api;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;
import java.util.stream.Stream;

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
import com.prgrms.himin.setup.domain.MenuSetUp;
import com.prgrms.himin.setup.domain.ShopSetUp;
import com.prgrms.himin.setup.request.ShopCreateRequestBuilder;
import com.prgrms.himin.setup.request.ShopUpdateRequestBuilder;
import com.prgrms.himin.shop.domain.Category;
import com.prgrms.himin.shop.domain.Shop;
import com.prgrms.himin.shop.domain.ShopRepository;
import com.prgrms.himin.shop.domain.ShopSort;
import com.prgrms.himin.shop.domain.ShopStatus;
import com.prgrms.himin.shop.dto.request.ShopCreateRequest;
import com.prgrms.himin.shop.dto.request.ShopUpdateRequest;

@Sql("/truncate.sql")
@SpringBootTest
@AutoConfigureMockMvc
class ShopControllerTest {

	final String BASE_URL = "/api/shops";

	@Autowired
	ShopRepository shopRepository;

	@Autowired
	MockMvc mvc;

	@Autowired
	ObjectMapper objectMapper;

	@Autowired
	ShopSetUp shopSetUp;

	@Autowired
	MenuSetUp menuSetUp;

	@Nested
	@DisplayName("가게 생성을 할 수 있다.")
	class CreateShop {

		private static Stream<Arguments> provideRequestForErrorValue() {
			return Stream.of(
				Arguments.of("가장 긴 가게 이름을 가지고 있는 맥도날드", "가게 이름은 최대 20글자 입니다."),
				Arguments.of(null, "이름이 비어있으면 안됩니다."),
				Arguments.of("", "이름이 비어있으면 안됩니다."),
				Arguments.of("  ", "이름이 비어있으면 안됩니다.")
			);
		}

		@DisplayName("성공한다.")
		@Test
		void success_test() throws Exception {
			// given
			ShopCreateRequest request = ShopCreateRequestBuilder.successBuild();

			String body = objectMapper.writeValueAsString(request);

			// when
			ResultActions resultAction = mvc.perform(post(BASE_URL)
					.content(body)
					.contentType(MediaType.APPLICATION_JSON))
				.andDo(print());

			// then
			resultAction.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("shopId").isNotEmpty())
				.andExpect(jsonPath("name").value(request.name()))
				.andExpect(jsonPath("category").value(request.category()))
				.andExpect(jsonPath("address").value(request.address()))
				.andExpect(jsonPath("phone").value(request.phone()))
				.andExpect(jsonPath("content").value(request.content()))
				.andExpect(jsonPath("deliveryTip").value(request.deliveryTip()))
				.andExpect(jsonPath("dibsCount").value(0))
				.andExpect(jsonPath("status").value(ShopStatus.CLOSE.toString()))
				.andExpect(jsonPath("openingTime").value(request.openingTime().toString()))
				.andExpect(jsonPath("closingTime").value(request.closingTime().toString()));
		}

		@DisplayName("유효하지 않은 요청값이 들어와서 실패한다.")
		@ParameterizedTest
		@MethodSource("provideRequestForErrorValue")
		void not_valid_request_fail_test(String input, String expected) throws Exception {
			// given
			ShopCreateRequest request = ShopCreateRequestBuilder.failBuild(input, Category.FAST_FOOD);

			String body = objectMapper.writeValueAsString(request);

			// when
			ResultActions resultAction = mvc.perform(post(BASE_URL)
					.content(body)
					.contentType(MediaType.APPLICATION_JSON))
				.andDo(print());

			// then
			resultAction.andExpect(status().isBadRequest())
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
	@DisplayName("가게를 조회할 수 있다.")
	class GetShop {

		final String GET_URL = BASE_URL + "/{shopId}";

		@DisplayName("성공한다.")
		@Test
		void success_test() throws Exception {
			// given
			Shop shop = shopSetUp.saveOne();

			// when
			ResultActions resultAction = mvc.perform(get(GET_URL, shop.getShopId()))
				.andDo(print());

			// then
			resultAction.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("shopId").value(shop.getShopId()))
				.andExpect(jsonPath("name").value(shop.getName()))
				.andExpect(jsonPath("category").value(shop.getCategory().toString()))
				.andExpect(jsonPath("address").value(shop.getAddress()))
				.andExpect(jsonPath("phone").value(shop.getPhone()))
				.andExpect(jsonPath("content").value(shop.getContent()))
				.andExpect(jsonPath("deliveryTip").value(shop.getDeliveryTip()))
				.andExpect(jsonPath("dibsCount").value(shop.getDibsCount()))
				.andExpect(jsonPath("status").value(shop.getStatus().toString()))
				.andExpect(jsonPath("openingTime").value(shop.getOpeningTime().toString()))
				.andExpect(jsonPath("closingTime").value(shop.getClosingTime().toString()));
		}

		@DisplayName("가게가 존재하지 않아서 실패한다.")
		@Test
		void not_exist_shop_fail_test() throws Exception {
			// given
			Long shopId = 0L;

			// when
			ResultActions resultAction = mvc.perform(get(GET_URL, shopId))
				.andDo(print());

			// then
			resultAction.andExpect(status().isNotFound())
				.andExpect((result) -> assertTrue(
					result.getResolvedException().getClass().isAssignableFrom((EntityNotFoundException.class))))
				.andExpect(jsonPath("error").value(ErrorCode.SHOP_NOT_FOUND.toString()))
				.andExpect(jsonPath("code").value(ErrorCode.SHOP_NOT_FOUND.getCode()))
				.andExpect(jsonPath("message").value(ErrorCode.SHOP_NOT_FOUND.getMessage()));
		}
	}

	@Nested
	@DisplayName("가게 목록을 조회할 수 있다.")
	class GetShops {

		@DisplayName("전체를 조회한다.")
		@Test
		void success_test() throws Exception {
			// given
			List<Shop> shops = shopSetUp.saveMany();

			// when
			ResultActions resultAction = mvc.perform(get(BASE_URL))
				.andDo(print());

			// then
			resultAction.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("shopResponses[0].shopId").value(shops.get(0).getShopId()))
				.andExpect(jsonPath("shopResponses[0].name").value(shops.get(0).getName()))
				.andExpect(jsonPath("shopResponses[0].category").value(shops.get(0).getCategory().toString()))
				.andExpect(jsonPath("shopResponses[0].address").value(shops.get(0).getAddress()))
				.andExpect(jsonPath("shopResponses[0].phone").value(shops.get(0).getPhone()))
				.andExpect(jsonPath("shopResponses[0].content").value(shops.get(0).getContent()))
				.andExpect(jsonPath("shopResponses[0].deliveryTip").value(shops.get(0).getDeliveryTip()))
				.andExpect(jsonPath("shopResponses[0].dibsCount").value(shops.get(0).getDibsCount()))
				.andExpect(jsonPath("shopResponses[0].status").value(shops.get(0).getStatus().toString()))
				.andExpect(jsonPath("shopResponses[0].openingTime").value(shops.get(0).getOpeningTime().toString()))
				.andExpect(jsonPath("shopResponses[0].closingTime").value(shops.get(0).getClosingTime().toString()))
				.andExpect(jsonPath("shopResponses[1].shopId").value(shops.get(1).getShopId()))
				.andExpect(jsonPath("shopResponses[1].name").value(shops.get(1).getName()))
				.andExpect(jsonPath("shopResponses[1].category").value(shops.get(1).getCategory().toString()))
				.andExpect(jsonPath("shopResponses[1].address").value(shops.get(1).getAddress()))
				.andExpect(jsonPath("shopResponses[1].phone").value(shops.get(1).getPhone()))
				.andExpect(jsonPath("shopResponses[1].content").value(shops.get(1).getContent()))
				.andExpect(jsonPath("shopResponses[1].deliveryTip").value(shops.get(1).getDeliveryTip()))
				.andExpect(jsonPath("shopResponses[1].dibsCount").value(shops.get(1).getDibsCount()))
				.andExpect(jsonPath("shopResponses[1].status").value(shops.get(1).getStatus().toString()))
				.andExpect(jsonPath("shopResponses[1].openingTime").value(shops.get(1).getOpeningTime().toString()))
				.andExpect(jsonPath("shopResponses[1].closingTime").value(shops.get(1).getClosingTime().toString()))
				.andExpect(jsonPath("size").value(10))
				.andExpect(jsonPath("nextCursor").value(shops.get(1).getShopId()))
				.andExpect(jsonPath("sort").doesNotExist())
				.andExpect(jsonPath("isLast").value(true));
		}

		@DisplayName("검색 조건에 따라 가게를 검색한다.")
		@Test
		void search_by_options_success_test() throws Exception {
			// given
			List<Shop> shops = shopSetUp.saveMany();

			String name = shops.get(1).getName();
			String searchOption = name.substring(name.length() / 2);

			// when
			ResultActions resultAction = mvc.perform(get(BASE_URL)
					.queryParam("name", searchOption))
				.andDo(print());

			// then
			resultAction.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("shopResponses[0].shopId").value(shops.get(1).getShopId()))
				.andExpect(jsonPath("shopResponses[0].name").value(shops.get(1).getName()))
				.andExpect(jsonPath("shopResponses[0].category").value(shops.get(1).getCategory().toString()))
				.andExpect(jsonPath("shopResponses[0].address").value(shops.get(1).getAddress()))
				.andExpect(jsonPath("shopResponses[0].phone").value(shops.get(1).getPhone()))
				.andExpect(jsonPath("shopResponses[0].content").value(shops.get(1).getContent()))
				.andExpect(jsonPath("shopResponses[0].deliveryTip").value(shops.get(1).getDeliveryTip()))
				.andExpect(jsonPath("shopResponses[0].dibsCount").value(shops.get(1).getDibsCount()))
				.andExpect(jsonPath("shopResponses[0].status").value(shops.get(1).getStatus().toString()))
				.andExpect(jsonPath("shopResponses[0].openingTime").value(shops.get(1).getOpeningTime().toString()))
				.andExpect(jsonPath("shopResponses[0].closingTime").value(shops.get(1).getClosingTime().toString()))
				.andExpect(jsonPath("size").value(10))
				.andExpect(jsonPath("nextCursor").value(shops.get(1).getShopId()))
				.andExpect(jsonPath("sort").doesNotExist())
				.andExpect(jsonPath("isLast").value(true));
		}

		@DisplayName("메뉴이름을 포함한 가게를 검색한다.")
		@Test
		void search_by_menuName_success_test() throws Exception {
			// given
			List<Shop> shops = shopSetUp.saveMany();
			Shop targetShop = shops.get(1);

			List<Menu> menus = menuSetUp.saveMany(targetShop);
			String menuName = menus.get(0).getName();
			String menuNameForOption = menuName.substring(menuName.length() / 2);

			// when
			ResultActions resultAction = mvc.perform(get(BASE_URL)
					.queryParam("menuName", menuNameForOption))
				.andDo(print());

			// then
			resultAction.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("shopResponses[0].shopId").value(shops.get(1).getShopId()))
				.andExpect(jsonPath("shopResponses[0].name").value(shops.get(1).getName()))
				.andExpect(jsonPath("shopResponses[0].category").value(shops.get(1).getCategory().toString()))
				.andExpect(jsonPath("shopResponses[0].address").value(shops.get(1).getAddress()))
				.andExpect(jsonPath("shopResponses[0].phone").value(shops.get(1).getPhone()))
				.andExpect(jsonPath("shopResponses[0].content").value(shops.get(1).getContent()))
				.andExpect(jsonPath("shopResponses[0].deliveryTip").value(shops.get(1).getDeliveryTip()))
				.andExpect(jsonPath("shopResponses[0].dibsCount").value(shops.get(1).getDibsCount()))
				.andExpect(jsonPath("shopResponses[0].status").value(shops.get(1).getStatus().toString()))
				.andExpect(jsonPath("shopResponses[0].openingTime").value(shops.get(1).getOpeningTime().toString()))
				.andExpect(jsonPath("shopResponses[0].closingTime").value(shops.get(1).getClosingTime().toString()))
				.andExpect(jsonPath("size").value(10))
				.andExpect(jsonPath("nextCursor").value(shops.get(1).getShopId()))
				.andExpect(jsonPath("sort").doesNotExist())
				.andExpect(jsonPath("isLast").value(true));
		}

		@DisplayName("정렬 조건을 통해 가게를 조회한다.")
		@Test
		void order_by_sort_success_test() throws Exception {
			// given
			List<Shop> shops = shopSetUp.saveMany();

			String sort = "deliveryTipAsc";

			// when
			ResultActions resultAction = mvc.perform(get(BASE_URL)
					.queryParam("sort", sort))
				.andDo(print());

			// then
			resultAction.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("shopResponses[0].shopId").value(shops.get(1).getShopId()))
				.andExpect(jsonPath("shopResponses[0].name").value(shops.get(1).getName()))
				.andExpect(jsonPath("shopResponses[0].category").value(shops.get(1).getCategory().toString()))
				.andExpect(jsonPath("shopResponses[0].address").value(shops.get(1).getAddress()))
				.andExpect(jsonPath("shopResponses[0].phone").value(shops.get(1).getPhone()))
				.andExpect(jsonPath("shopResponses[0].content").value(shops.get(1).getContent()))
				.andExpect(jsonPath("shopResponses[0].deliveryTip").value(shops.get(1).getDeliveryTip()))
				.andExpect(jsonPath("shopResponses[0].dibsCount").value(shops.get(1).getDibsCount()))
				.andExpect(jsonPath("shopResponses[0].status").value(shops.get(1).getStatus().toString()))
				.andExpect(jsonPath("shopResponses[0].openingTime").value(shops.get(1).getOpeningTime().toString()))
				.andExpect(jsonPath("shopResponses[0].closingTime").value(shops.get(1).getClosingTime().toString()))
				.andExpect(jsonPath("shopResponses[1].shopId").value(shops.get(0).getShopId()))
				.andExpect(jsonPath("shopResponses[1].name").value(shops.get(0).getName()))
				.andExpect(jsonPath("shopResponses[1].category").value(shops.get(0).getCategory().toString()))
				.andExpect(jsonPath("shopResponses[1].address").value(shops.get(0).getAddress()))
				.andExpect(jsonPath("shopResponses[1].phone").value(shops.get(0).getPhone()))
				.andExpect(jsonPath("shopResponses[1].content").value(shops.get(0).getContent()))
				.andExpect(jsonPath("shopResponses[1].deliveryTip").value(shops.get(0).getDeliveryTip()))
				.andExpect(jsonPath("shopResponses[1].dibsCount").value(shops.get(0).getDibsCount()))
				.andExpect(jsonPath("shopResponses[1].status").value(shops.get(0).getStatus().toString()))
				.andExpect(jsonPath("shopResponses[1].openingTime").value(shops.get(0).getOpeningTime().toString()))
				.andExpect(jsonPath("shopResponses[1].closingTime").value(shops.get(0).getClosingTime().toString()))
				.andExpect(jsonPath("size").value(10))
				.andExpect(jsonPath("nextCursor").value(shops.get(0).getShopId()))
				.andExpect(jsonPath("sort").value(ShopSort.from(sort).toString()))
				.andExpect(jsonPath("isLast").value(true));
		}

		@DisplayName("커서를 통해 가게를 조회한다.")
		@Test
		void cursor_success_test() throws Exception {
			// given
			List<Shop> shops = shopSetUp.saveMany();

			Long cursor = shops.get(1).getShopId() - 1;

			// when
			ResultActions resultAction = mvc.perform(get(BASE_URL)
					.queryParam("cursor", String.valueOf(cursor)))
				.andDo(print());

			// then
			resultAction.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("shopResponses[0].shopId").value(shops.get(1).getShopId()))
				.andExpect(jsonPath("shopResponses[0].name").value(shops.get(1).getName()))
				.andExpect(jsonPath("shopResponses[0].category").value(shops.get(1).getCategory().toString()))
				.andExpect(jsonPath("shopResponses[0].address").value(shops.get(1).getAddress()))
				.andExpect(jsonPath("shopResponses[0].phone").value(shops.get(1).getPhone()))
				.andExpect(jsonPath("shopResponses[0].content").value(shops.get(1).getContent()))
				.andExpect(jsonPath("shopResponses[0].deliveryTip").value(shops.get(1).getDeliveryTip()))
				.andExpect(jsonPath("shopResponses[0].dibsCount").value(shops.get(1).getDibsCount()))
				.andExpect(jsonPath("shopResponses[0].status").value(shops.get(1).getStatus().toString()))
				.andExpect(jsonPath("shopResponses[0].openingTime").value(shops.get(1).getOpeningTime().toString()))
				.andExpect(jsonPath("shopResponses[0].closingTime").value(shops.get(1).getClosingTime().toString()))
				.andExpect(jsonPath("size").value(10))
				.andExpect(jsonPath("nextCursor").value(shops.get(1).getShopId()))
				.andExpect(jsonPath("sort").doesNotExist())
				.andExpect(jsonPath("isLast").value(true));
		}
	}

	@Nested
	@DisplayName("가게 정보를 수정할 수 있다.")
	class UpdateShop {

		final String PUT_URL = BASE_URL + "/{shopId}";

		private static Stream<Arguments> provideRequestForErrorValue() {
			return Stream.of(
				Arguments.of("가장 긴 가게 이름을 가지고 있는 맥도날드", "가게 이름은 최대 20글자 입니다."),
				Arguments.of(null, "이름이 비어있으면 안됩니다."),
				Arguments.of("", "이름이 비어있으면 안됩니다."),
				Arguments.of("  ", "이름이 비어있으면 안됩니다.")
			);
		}

		@DisplayName("성공한다.")
		@Test
		void success_test() throws Exception {
			// given
			Shop shop = shopSetUp.saveOne();

			ShopUpdateRequest.Info request = ShopUpdateRequestBuilder.infoSuccessBuild();

			String body = objectMapper.writeValueAsString(request);

			// when
			ResultActions resultAction = mvc.perform(put(PUT_URL, shop.getShopId())
					.content(body)
					.contentType(MediaType.APPLICATION_JSON))
				.andDo(print());

			// then
			resultAction.andExpect(status().isNoContent());

			Shop foundShop = shopRepository.findById(shop.getShopId()).get();
			assertThat(foundShop.getName()).isEqualTo(request.name());
			assertThat(foundShop.getCategory().toString()).isEqualTo(request.category());
			assertThat(foundShop.getAddress()).isEqualTo(request.address());
			assertThat(foundShop.getPhone()).isEqualTo(request.phone());
			assertThat(foundShop.getContent()).isEqualTo(request.content());
			assertThat(foundShop.getDeliveryTip()).isEqualTo(request.deliveryTip());
			assertThat(foundShop.getOpeningTime()).isEqualTo(request.openingTime());
			assertThat(foundShop.getClosingTime()).isEqualTo(request.closingTime());
		}

		@DisplayName("가게가 존재하지 않아서 실패한다.")
		@Test
		void not_exist_shop_fail_test() throws Exception {
			// given
			Long shopId = 0L;

			ShopUpdateRequest.Info request = ShopUpdateRequestBuilder.infoSuccessBuild();

			String body = objectMapper.writeValueAsString(request);

			// when
			ResultActions resultAction = mvc.perform(put(PUT_URL, shopId)
					.content(body)
					.contentType(MediaType.APPLICATION_JSON))
				.andDo(print());

			// then
			resultAction.andExpect(status().isNotFound())
				.andExpect((result) -> assertTrue(
					result.getResolvedException().getClass().isAssignableFrom((EntityNotFoundException.class))))
				.andExpect(jsonPath("error").value(ErrorCode.SHOP_NOT_FOUND.toString()))
				.andExpect(jsonPath("code").value(ErrorCode.SHOP_NOT_FOUND.getCode()))
				.andExpect(jsonPath("message").value(ErrorCode.SHOP_NOT_FOUND.getMessage()));
		}

		@DisplayName("잘못된 가게 정보가 들어와서 실패한다.")
		@ParameterizedTest
		@MethodSource("provideRequestForErrorValue")
		void not_valid_request_fail_test(String input, String expected) throws Exception {
			// given
			Shop shop = shopSetUp.saveOne();

			ShopUpdateRequest.Info request = ShopUpdateRequestBuilder.infoFailBuild(input);

			String body = objectMapper.writeValueAsString(request);

			// when
			ResultActions resultAction = mvc.perform(put(PUT_URL, shop.getShopId())
					.content(body)
					.contentType(MediaType.APPLICATION_JSON))
				.andDo(print());

			// then
			resultAction.andExpect(status().isBadRequest())
				.andExpect((result) -> assertTrue(
					result.getResolvedException().getClass().isAssignableFrom((MethodArgumentNotValidException.class))))
				.andExpect(jsonPath("error").value(ErrorCode.INVALID_REQUEST.toString()))
				.andExpect(jsonPath("errors[0].field").value("name"))
				.andExpect(jsonPath("errors[0].value").value(input))
				.andExpect(jsonPath("$.errors[0].reason").value(expected))
				.andExpect(jsonPath("code").value(ErrorCode.INVALID_REQUEST.getCode()))
				.andExpect(jsonPath("message").value(ErrorCode.INVALID_REQUEST.getMessage()));
		}
	}

	@Nested
	@DisplayName("가게 상태를 변경할 수 있다.")
	class ChangeShopStatus {

		final String PATCH_URL = BASE_URL + "/{shopId}";

		@DisplayName("성공한다.")
		@Test
		void success_test() throws Exception {
			// given
			Shop shop = shopSetUp.saveOne();

			ShopUpdateRequest.Status request = ShopUpdateRequestBuilder.statusSuccessBuild();

			String body = objectMapper.writeValueAsString(request);

			// when
			ResultActions resultAction = mvc.perform(patch(PATCH_URL, shop.getShopId())
					.content(body)
					.contentType(MediaType.APPLICATION_JSON))
				.andDo(print());

			// then
			resultAction.andExpect(status().isNoContent());

			Shop foundShop = shopRepository.findById(shop.getShopId()).get();
			ShopStatus result = foundShop.getStatus();
			assertThat(result).isEqualTo(ShopStatus.OPEN);
		}

		@DisplayName("가게가 존재하지 않아서 실패한다.")
		@Test
		void not_exist_shop_fail_test() throws Exception {
			// given
			Long shopId = 0L;

			ShopUpdateRequest.Status request = ShopUpdateRequestBuilder.statusSuccessBuild();

			String body = objectMapper.writeValueAsString(request);

			// when
			ResultActions resultAction = mvc.perform(patch(PATCH_URL, shopId)
					.content(body)
					.contentType(MediaType.APPLICATION_JSON))
				.andDo(print());

			// then
			resultAction.andExpect(status().isNotFound())
				.andExpect((result) -> assertTrue(
					result.getResolvedException().getClass().isAssignableFrom((EntityNotFoundException.class))))
				.andExpect(jsonPath("error").value(ErrorCode.SHOP_NOT_FOUND.toString()))
				.andExpect(jsonPath("code").value(ErrorCode.SHOP_NOT_FOUND.getCode()))
				.andExpect(jsonPath("message").value(ErrorCode.SHOP_NOT_FOUND.getMessage()));
		}

		@DisplayName("잘못된 가게 상태 정보가 들어와서 실패한다.")
		@Test
		void not_valid_request_fail_test() throws Exception {
			// given
			Shop shop = shopSetUp.saveOne();

			ShopUpdateRequest.Status request = ShopUpdateRequestBuilder.statusFailBuild("READY");

			String body = objectMapper.writeValueAsString(request);

			// when
			ResultActions resultAction = mvc.perform(patch(PATCH_URL, shop.getShopId())
					.content(body)
					.contentType(MediaType.APPLICATION_JSON))
				.andDo(print());

			// then
			resultAction.andExpect(status().isBadRequest())
				.andExpect((result) -> assertTrue(
					result.getResolvedException().getClass().isAssignableFrom((MethodArgumentNotValidException.class))))
				.andExpect(jsonPath("error").value(ErrorCode.INVALID_REQUEST.toString()))
				.andExpect(jsonPath("errors[0].field").value("status"))
				.andExpect(jsonPath("errors[0].value").value(request.status()))
				.andExpect(jsonPath("$.errors[0].reason").value("유효하지 않은 Enum 입니다."))
				.andExpect(jsonPath("code").value(ErrorCode.INVALID_REQUEST.getCode()))
				.andExpect(jsonPath("message").value(ErrorCode.INVALID_REQUEST.getMessage()));
		}
	}

	@Nested
	@DisplayName("가게를 삭제할 수 있다.")
	class DeleteShop {

		final String DELETE_URL = BASE_URL + "/{shopId}";

		@DisplayName("성공한다.")
		@Test
		void success_test() throws Exception {
			// given
			Shop shop = shopSetUp.saveOne();

			// when
			ResultActions resultAction = mvc.perform(delete(DELETE_URL, shop.getShopId()))
				.andDo(print());

			// then
			resultAction.andExpect(status().isOk());

			boolean result = shopRepository.existsById(shop.getShopId());
			assertThat(result).isFalse();
		}

		@DisplayName("가게가 존재하지 않아서 실패한다.")
		@Test
		void not_exist_shop_fail_test() throws Exception {
			// given
			Long shopId = 0L;

			// when
			ResultActions resultAction = mvc.perform(delete(DELETE_URL, shopId))
				.andDo(print());

			// then
			resultAction.andExpect(status().isNotFound())
				.andExpect((result) -> assertTrue(
					result.getResolvedException().getClass().isAssignableFrom((EntityNotFoundException.class))))
				.andExpect(jsonPath("error").value(ErrorCode.SHOP_NOT_FOUND.toString()))
				.andExpect(jsonPath("code").value(ErrorCode.SHOP_NOT_FOUND.getCode()))
				.andExpect(jsonPath("message").value(ErrorCode.SHOP_NOT_FOUND.getMessage()));
		}
	}
}
