package sample.coffeekiosk.domain.product;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static sample.coffeekiosk.domain.product.ProductSellingStatus.*;
import static sample.coffeekiosk.domain.product.ProductType.HANDMADE;

@Transactional
@SpringBootTest
@ActiveProfiles("test")
class ProductRepositoryTest {

    @Autowired
    private ProductRepository productRepository;

    @DisplayName("가장 마지막에 저장된 상품 번호를 가져온다.")
    @Test
    void findLatestProductNumber() {

        String targetProductNumber = "003";
        // given
        Product product1 = Product.create("001", HANDMADE, SELLING, "아메리카노", 4000);
        Product product2 = Product.create("002", HANDMADE, HOLD, "카페라떼", 4500);
        Product product3 = Product.create(targetProductNumber, HANDMADE, STOP_SELLING, "팥빙수", 700);

        List<Product> products = productRepository.saveAll(List.of(product1, product2, product3));

        // when
        String latestProductNumber = productRepository.findLatestProductNumber().get();

        // then
        assertThat(latestProductNumber).isEqualTo(targetProductNumber);
    }

    @DisplayName("가장 마지막에 저장된 상품 번호를 가져 올때 상품이 하나도 없는 경우는 null 을 반환한다.")
    @Test
    void findLatestProductNumberWhenProductIsEmpty() {

        // given & when
        String latestProductNumber = productRepository.findLatestProductNumber().orElseGet(() -> null);

        // then
        assertThat(latestProductNumber).isNull();
    }


    @DisplayName("원하는 판매 상태를 가진 상품들을 조회한다.")
    @Test
    void findAllBySellingStatusIn() {
        // given
        Product product1 = Product.builder()
                .productNumber("001")
                .type(HANDMADE)
                .sellingStatus(SELLING)
                .name("아메리카노")
                .price(4000)
                .build();

        Product product2 = Product.builder()
                .productNumber("002")
                .type(HANDMADE)
                .sellingStatus(HOLD)
                .name("카페라떼")
                .price(4500)
                .build();

        Product product3 = Product.builder()
                .productNumber("003")
                .type(HANDMADE)
                .sellingStatus(STOP_SELLING)
                .name("팥빙수")
                .price(7000)
                .build();

        productRepository.saveAll(List.of(product1, product2, product3));

        // when
        List<Product> products = productRepository.findAllBySellingStatusIn(List.of(SELLING, HOLD));

        // list 값을 검증할때는 아래처럼 하는게 좋다.
        // then
        assertThat(products).hasSize(2)
                .extracting("productNumber", "name", "sellingStatus")
                .containsExactlyInAnyOrder(
                        tuple("001", "아메리카노", SELLING),
                        tuple("002", "카페라떼", HOLD)
                );
    }
}