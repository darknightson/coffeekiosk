package sample.coffeekiosk.api.service.order;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import sample.coffeekiosk.api.controller.order.request.OrderCreateRequest;
import sample.coffeekiosk.api.service.order.response.OrderResponse;
import sample.coffeekiosk.domain.product.Product;
import sample.coffeekiosk.domain.product.ProductRepository;
import sample.coffeekiosk.domain.product.ProductType;
import sample.coffeekiosk.domain.stock.Stock;
import sample.coffeekiosk.domain.stock.StockRepository;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static sample.coffeekiosk.domain.product.ProductSellingStatus.SELLING;
import static sample.coffeekiosk.domain.product.ProductType.*;

@ActiveProfiles("test")
@Transactional
@SpringBootTest
class OrderServiceTest {

    @Autowired
    private OrderService orderService;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private StockRepository stockRepository;

    @DisplayName("주문번호 리스트를 받아 주문을 생성한다.")
    @Test
    void createOrder() {

        // given
        LocalDateTime registerDateTime = LocalDateTime.now();

        Product product1 = createProduct(HANDMADE, "001", 1000);
        Product product2 = createProduct(HANDMADE, "002", 3000);
        Product product3 = createProduct(HANDMADE, "003", 5000);

        productRepository.saveAll(List.of(product1, product2, product3));

        OrderCreateRequest request = OrderCreateRequest.builder()
                .productNumbers(List.of("001", "002"))
                .build();

        // when
        OrderResponse orderResponse = orderService.createOrder(request, registerDateTime);
        // then
        assertThat(orderResponse.getId()).isNotNull();
        assertThat(orderResponse)
                .extracting("registeredDateTime", "totalPrice")
                .contains(registerDateTime, 4000);


        assertThat(orderResponse.getProducts()).hasSize(2)
                .extracting("productNumber", "price")
                .containsExactlyInAnyOrder(
                        tuple("001", 1000),
                        tuple("002", 3000)
                );
    }

    @DisplayName("등일한 상품을 2개 주문한다. 이때 주문이 잘되는지 검증한다.")
    @Test
    void createOrderWithDuplicateProductNumber() {

        // given
        LocalDateTime registerDateTime = LocalDateTime.now();

        Product product1 = createProduct(HANDMADE, "001", 1000);
        Product product2 = createProduct(HANDMADE, "002", 3000);
        Product product3 = createProduct(HANDMADE, "003", 5000);

        productRepository.saveAll(List.of(product1, product2, product3));

        OrderCreateRequest request = OrderCreateRequest.builder()
                .productNumbers(List.of("001", "001"))
                .build();

        // when
        OrderResponse orderResponse = orderService.createOrder(request, registerDateTime);
        // then
        assertThat(orderResponse.getId()).isNotNull();
        assertThat(orderResponse)
                .extracting("registeredDateTime", "totalPrice")
                .contains(registerDateTime, 2000);


        assertThat(orderResponse.getProducts()).hasSize(2)
                .extracting("productNumber", "price")
                .containsExactlyInAnyOrder(
                        tuple("001", 1000),
                        tuple("001", 1000)
                );
    }

    @DisplayName("재고와 관련된 상품이 포함되어 있는 주문번호 리스트를 받아 주문을 생성한다.")
    @Test
    void createOrderWithStock() {

        // given
        LocalDateTime registerDateTime = LocalDateTime.now();
        // 테스트용 상품 생성
        Product product1 = createProduct(BOTTLE,"001", 1000);
        Product product2 = createProduct(BAKERY, "002", 3000);
        Product product3 = createProduct(HANDMADE, "003", 5000);

        productRepository.saveAll(List.of(product1, product2, product3));

        // 재고 생성
        Stock stock1 = Stock.create("001", 2);
        Stock stock2 = Stock.create("002", 2);

        List<Stock> stocks = stockRepository.saveAll(List.of(stock1, stock2));

        OrderCreateRequest request = OrderCreateRequest.builder()
                .productNumbers(List.of("001", "001", "002", "003"))
                .build();

        // when
        OrderResponse orderResponse = orderService.createOrder(request, registerDateTime);
        // then
        assertThat(orderResponse.getId()).isNotNull();
        assertThat(orderResponse)
                .extracting("registeredDateTime", "totalPrice")
                .contains(registerDateTime, 10000);



        assertThat(orderResponse.getProducts()).hasSize(4)
                .extracting("productNumber", "price")
                .containsExactlyInAnyOrder(
                        tuple("001", 1000),
                        tuple("001", 1000),
                        tuple("002", 3000),
                        tuple("003", 5000)
                );

        assertThat(stocks).hasSize(2)
                .extracting("productNumber", "quantity")
                .containsExactlyInAnyOrder(
                        tuple("001", 0),
                        tuple("002", 1)
                );
    }

    @DisplayName("재고가 부족한 상품으로 주문을 생성 하려는 경우 예외가 발생한다.")
    @Test
    void createOrderWithNoStock() {

        // given
        LocalDateTime registerDateTime = LocalDateTime.now();
        // 테스트용 상품 생성
        Product product1 = createProduct(BOTTLE,"001", 1000);
        Product product2 = createProduct(BAKERY, "002", 3000);
        Product product3 = createProduct(HANDMADE, "003", 5000);

        productRepository.saveAll(List.of(product1, product2, product3));

        // 재고 생성
        Stock stock1 = Stock.create("001", 1);
        Stock stock2 = Stock.create("002", 1);

        List<Stock> stocks = stockRepository.saveAll(List.of(stock1, stock2));

        OrderCreateRequest request = OrderCreateRequest.builder()
                .productNumbers(List.of("001", "001", "002", "003"))
                .build();

        // when & then
        assertThatThrownBy(() -> orderService.createOrder(request, registerDateTime))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("재고가 부족합니다.");
    }

    Product createProduct(ProductType productType, String productNumber, int price) {
        return Product.builder()
                .productNumber(productNumber)
                .type(productType)
                .sellingStatus(SELLING)
                .name("test product")
                .price(price)
                .build();
    }

    @DisplayName("1111")
    @Test
    void Test() {
        String a = "1";
        /*
        StringUtils.hasText(값); 을 사용하면
        값이 있을 경우에는 true를 반환하고
        공백이나 NULL이 들어올 경우에는
        false를 반환하게 된다
         */
        if (!StringUtils.hasText(a)) {
            System.out.println("a = " + a);
        }

    }


}