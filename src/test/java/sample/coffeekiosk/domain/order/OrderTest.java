package sample.coffeekiosk.domain.order;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import sample.coffeekiosk.domain.product.Product;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static sample.coffeekiosk.domain.order.OrderStatus.INIT;
import static sample.coffeekiosk.domain.product.ProductSellingStatus.SELLING;
import static sample.coffeekiosk.domain.product.ProductType.HANDMADE;

class OrderTest {

    @DisplayName("주 생성 시 상품 리스트에서 주문의 총 금액을 계산한다.")
    @Test
    void calculateTotalPriceTest() {

        // given
        List<Product> products = List.of(
                createProduct("001", 1000),
                createProduct("002", 2000)
        );

        // when
        Order order = Order.create(products, LocalDateTime.now());

        // then
        assertThat(order.getTotalPrice()).isEqualTo(3000);
    }

    @DisplayName("주문 생성 시 주문 상태는 INIT 이다.")
    @Test
    void orderInitTest() {

        // given
        List<Product> products = List.of(
                createProduct("001", 1000),
                createProduct("002", 2000)
        );

        // when
        Order order = Order.create(products, LocalDateTime.now());

        // then
        assertThat(order.getOrderStatus()).isEqualTo(INIT);
    }
    
    @DisplayName("주문 생성 시 주문 등록 시간을 기록한다.")
    @Test
    void registeredDateTimeTest() {
        LocalDateTime registerDateTime = LocalDateTime.now();
        // given
        List<Product> products = List.of(
                createProduct("001", 1000),
                createProduct("002", 2000)
        );
        
        // when
        Order order = Order.create(products, registerDateTime);

        // then
        assertThat(order.getRegisterDateTime()).isEqualTo(registerDateTime);
    }
    
    

    Product createProduct(String productNumber, int price) {
        return Product.builder()
                .productNumber(productNumber)
                .type(HANDMADE)
                .sellingStatus(SELLING)
                .name("test product")
                .price(price)
                .build();
    }


}