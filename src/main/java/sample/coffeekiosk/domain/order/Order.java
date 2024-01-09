package sample.coffeekiosk.domain.order;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import sample.coffeekiosk.domain.BaseEntity;
import sample.coffeekiosk.domain.orderproduct.OrderProduct;
import sample.coffeekiosk.domain.product.Product;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "orders")
public class Order extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    private int totalPrice;

    private LocalDateTime registerDateTime;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderProduct> orderProducts = new ArrayList<>();

    @Builder
    private Order(List<Product> products, LocalDateTime registerDateTime, OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
        this.totalPrice = calculateTotalPrice(products);
        this.orderProducts = products.stream()
                .map(product -> new OrderProduct(this, product))
                .collect(toList());
        this.registerDateTime = registerDateTime;
    }

    public static Order create(List<Product> products, LocalDateTime registerDateTime) {
        return new Order(products, registerDateTime, OrderStatus.INIT);
    }

    public static Order createPaymentComplete(List<Product> products, LocalDateTime registerDateTime, OrderStatus orderStatus) {
        return new Order(products, registerDateTime, orderStatus);
    }

    private static int calculateTotalPrice(List<Product> products) {
        return products.stream()
                .mapToInt(Product::getPrice)
                .sum();
    }
}
