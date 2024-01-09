package sample.coffeekiosk.api.service.order;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.transaction.annotation.Transactional;
import sample.coffeekiosk.client.mail.MailSendClient;
import sample.coffeekiosk.domain.history.mail.MailSendHistory;
import sample.coffeekiosk.domain.history.mail.MailSendHistoryRepository;
import sample.coffeekiosk.domain.order.Order;
import sample.coffeekiosk.domain.order.OrderRepository;
import sample.coffeekiosk.domain.order.OrderStatus;
import sample.coffeekiosk.domain.product.Product;
import sample.coffeekiosk.domain.product.ProductRepository;
import sample.coffeekiosk.domain.product.ProductType;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static sample.coffeekiosk.domain.product.ProductSellingStatus.SELLING;
import static sample.coffeekiosk.domain.product.ProductType.HANDMADE;

@Transactional
@SpringBootTest
class OrderStatisticsServiceTest {

    @Autowired
    private OrderStatisticsService orderStatisticsService;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private MailSendHistoryRepository mailSendHistoryRepository;

    @MockBean
    private MailSendClient mailSendClient;

    @DisplayName("결제완료 주문들을 조회하여 매출 통계 메일을 전송한다.")
    @Test
    void sendOrderStatisticsMail() {
        // given
        LocalDateTime now = LocalDateTime.of(2023, 3, 5, 0, 0);

        Product product1 = createProduct(HANDMADE, "001", 1000);
        Product product2 = createProduct(HANDMADE, "002", 2000);
        Product product3 = createProduct(HANDMADE, "003", 3000);
        List<Product> products = List.of(product1, product2, product3);

        productRepository.saveAll(products);

        Order order1 = Order.createPaymentComplete(products, LocalDateTime.of(2023, 3, 4, 23, 59, 59), OrderStatus.PAYMENT_COMPLETED);
        Order order2 = Order.createPaymentComplete(products, now, OrderStatus.PAYMENT_COMPLETED);
        Order order3 = Order.createPaymentComplete(products, LocalDateTime.of(2023, 3, 5, 23, 59, 59), OrderStatus.PAYMENT_COMPLETED);
        Order order4 = Order.createPaymentComplete(products, LocalDateTime.of(2023, 3, 6, 0, 0), OrderStatus.PAYMENT_COMPLETED);
        orderRepository.saveAll(List.of(order1, order2, order3, order4));

        // stubbing -> 목객체에 원하는 행위를 지정한다. (when -> thenReturn)
        when(mailSendClient.sendEmail(any(String.class), any(String.class), any(String.class), any(String.class)))
                .thenReturn(true);

        // when
        boolean result = orderStatisticsService.sendOrderStatisticsMail(LocalDate.of(2023, 3, 5), "test@test.com");

        // then
        assertThat(result).isTrue();

        List<MailSendHistory> histories = mailSendHistoryRepository.findAll();
        assertThat(histories).hasSize(1)
                .extracting("content")
                .contains("총 매출 합계는 12000원입니다.");
    }
    private Product createProduct(ProductType type, String productNumber, int price) {
        return Product.builder()
                .type(type)
                .productNumber(productNumber)
                .price(price)
                .sellingStatus(SELLING)
                .name("메뉴 이름")
                .build();
    }

}