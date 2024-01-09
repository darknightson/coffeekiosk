package sample.coffeekiosk.api.controller.order;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import sample.coffeekiosk.api.controller.order.request.OrderCreateRequest;
import sample.coffeekiosk.api.service.order.OrderService;
import sample.coffeekiosk.api.service.order.response.OrderResponse;

import java.time.LocalDateTime;

@RestController
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/api/v1/orders/new")
    public OrderResponse createOrder(@RequestBody OrderCreateRequest request) {
        LocalDateTime registerDateTime = LocalDateTime.now();
        return orderService.createOrder(request, registerDateTime);
    }
}
