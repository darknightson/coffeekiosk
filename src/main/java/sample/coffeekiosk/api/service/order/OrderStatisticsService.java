package sample.coffeekiosk.api.service.order;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import sample.coffeekiosk.api.service.mail.MailService;
import sample.coffeekiosk.domain.order.Order;
import sample.coffeekiosk.domain.order.OrderRepository;
import sample.coffeekiosk.domain.order.OrderStatus;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class OrderStatisticsService {

    private final OrderRepository orderRepository;
    private final MailService mailService;
    public boolean sendOrderStatisticsMail(LocalDate orderDate, String email) {

        // 해당 일자에 결제 완료된 주문들을 가져와서
        List<Order> orders = orderRepository.findOrderBy(
                orderDate.atStartOfDay(),
                orderDate.plusDays(1).atStartOfDay(),
                OrderStatus.PAYMENT_COMPLETED
        );

        int totalAmount = orders.stream()
                .mapToInt(Order::getTotalPrice)
                .sum();

        // 메일 전송
        boolean result = mailService.sendMail(
                "no-reply@cafekiosk.com",
                email,
                String.format("[매출통계] %s", orderDate),
                String.format("총 매출 합계는 %s원입니다.", totalAmount)
        );

        if (!result) {
            throw new IllegalArgumentException("매출 통계 메일 전송에 실패했습니다.");
        }

        return true;

    }
}
