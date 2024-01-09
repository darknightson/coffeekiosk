package sample.coffeekiosk.api.controller.product;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import sample.coffeekiosk.api.controller.product.dto.request.ProductCreateRequest;
import sample.coffeekiosk.api.service.product.ProductService;
import sample.coffeekiosk.api.service.product.response.ProductResponse;
import sample.coffeekiosk.domain.product.ProductSellingStatus;
import sample.coffeekiosk.domain.product.ProductType;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

// 아래 주석은 통합 테스트를 할걸데 모두 자동으로 가짜 목을 등록한다.
//@AutoConfigureMockMvc
//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@WebMvcTest(controllers = ProductController.class)
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ProductService productService;

    @DisplayName("json 데이터를 테스트한다. 에러상태")
    @Test
    void jsonDataErrorTest() throws Exception {

//
//        ResultActions resultActions = mockMvc.perform(get("/some-endpoint"));
//
//        // HTTP 상태 코드와 JSON 응답 내용을 검증합니다.
//        resultActions.andExpect(status().isOk())
//                .andExpect(jsonPath("$.test").value("name"));

        // given , when , then
        // 아래는 ResultActions 를 이용하여 제이슨 데이터를 확인한다.
        ResultActions resultActions = mockMvc.perform(
                        get("/api/v1/products/test/error")
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                //.andExpect(jsonPath("$.code").value("name"))

                ;
        String contentAsString = resultActions.andReturn().getResponse().getContentAsString();
        System.out.println("contentAsString = " + contentAsString);
    }

    @DisplayName("json 데이터를 테스트한다.")
    @Test
    void jsonDataTest() throws Exception {

//
//        ResultActions resultActions = mockMvc.perform(get("/some-endpoint"));
//
//        // HTTP 상태 코드와 JSON 응답 내용을 검증합니다.
//        resultActions.andExpect(status().isOk())
//                .andExpect(jsonPath("$.test").value("name"));

        // given , when , then
        // 아래는 ResultActions 를 이용하여 제이슨 데이터를 확인한다.
        ResultActions resultActions = mockMvc.perform(
                        get("/api/v1/products/test")
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.test").value("name"))

                ;
        String contentAsString = resultActions.andReturn().getResponse().getContentAsString();
        System.out.println("contentAsString = " + contentAsString);
    }


    @DisplayName("신규 상품을 등록한다.")
    @Test
    void createProduct() throws Exception {

        // given
        ProductCreateRequest request = ProductCreateRequest.builder()
                .type(ProductType.HANDMADE)
                .sellingStatus(ProductSellingStatus.SELLING)
                .name("아메리카노")
                .price(4000)
                .build();

        // when & then
        mockMvc.perform(
                post("/api/v1/product/new")
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON)
        )
                .andDo(print())
                .andExpect(status().isOk());
    }

    @DisplayName("신규 상품을 등록한다.")
    @Test
    void createProductWithoutType() throws Exception {

        // given
        ProductCreateRequest request = ProductCreateRequest.builder()
                .sellingStatus(ProductSellingStatus.SELLING)
                .name("아메리카노")
                .price(4000)
                .build();

        // when & then
        mockMvc.perform(
                        post("/api/v1/product/new")
                                .content(objectMapper.writeValueAsString(request))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(400))
                .andExpect(jsonPath("$.httpStatus").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.message").value("상품 타입은 필수입니다."))
                .andExpect(jsonPath("$.data").isEmpty());

    }

    @DisplayName("판매 상품을 조회한다.")
    @Test
    void getSellingProducts() throws Exception {

        // given
        List<ProductResponse> of = List.of();

        when(productService.getSellingProducts()).thenReturn(of);

        /* when & then */
        mockMvc.perform(
                get("/api/v1/products/selling")
            )
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.code").value(200))
            .andExpect(jsonPath("$.httpStatus").value("OK"))
            .andExpect(jsonPath("$.message").value("OK"))
            .andExpect(jsonPath("$.data").isArray());

    }

}