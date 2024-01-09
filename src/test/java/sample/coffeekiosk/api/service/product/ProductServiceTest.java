package sample.coffeekiosk.api.service.product;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import sample.coffeekiosk.api.controller.product.dto.request.ProductCreateRequest;
import sample.coffeekiosk.api.service.product.response.ProductResponse;
import sample.coffeekiosk.domain.product.Product;
import sample.coffeekiosk.domain.product.ProductRepository;
import sample.coffeekiosk.domain.product.ProductSellingStatus;
import sample.coffeekiosk.domain.product.ProductType;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static sample.coffeekiosk.domain.product.ProductSellingStatus.*;
import static sample.coffeekiosk.domain.product.ProductType.HANDMADE;

@Transactional
@SpringBootTest
class ProductServiceTest {

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductRepository productRepository;

    @DisplayName("신규 상품을 등록한다. 상품 번호는 가장 최근 상품의 상품번호에서 1 증가한 값이다.")
    @Test
    void createProduct() {
        // given
        Product product1 = Product.create("001", HANDMADE, SELLING, "아메리카노", 4000);
        Product product2 = Product.create("002", HANDMADE, HOLD, "카페라떼", 4500);
        Product product3 = Product.create("003", HANDMADE, STOP_SELLING, "팥빙수", 700);

        ProductCreateRequest request = ProductCreateRequest.builder()
                .type(HANDMADE)
                .sellingStatus(SELLING)
                .name("카푸치노")
                .price(8000)
                .build();

        productRepository.saveAll(List.of(product1, product2, product3));

        String latestProductNumber = productRepository.findLatestProductNumber().get();

        // when
        ProductResponse productResponse = productService.createProduct(request);

        // then
        assertThat(productResponse)
                .extracting("productNumber", "type", "sellingStatus", "name", "price")
                .containsExactly(String.format("%03d", Integer.parseInt(latestProductNumber) + 1), HANDMADE, SELLING, "카푸치노", 8000);

    }

    @DisplayName("상품이 하나도 없는 경우 신규 상품을 등록하면 상품번호는 001 이다.")
    @Test
    void createProductWhenProductIs001() {

        ProductCreateRequest request = ProductCreateRequest.builder()
                .type(HANDMADE)
                .sellingStatus(SELLING)
                .name("카푸치노")
                .price(8000)
                .build();

        ProductResponse productResponse = productService.createProduct(request);
        // then
        assertThat(productResponse.getProductNumber()).isEqualTo("001");
    }



}