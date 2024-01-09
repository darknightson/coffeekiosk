package sample.coffeekiosk.domain.order;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findByOrderStatusAndRegisterDateTime(OrderStatus orderStatus, LocalDateTime registerDateTime);

    @Query(" select o " +
            "  from Order o " +
            " where o.registerDateTime >= :startDateTime " +
            "   and o.registerDateTime < :endDateTime " +
            "   and o.orderStatus = :orderStatus")
    List<Order> findOrderBy(LocalDateTime startDateTime, LocalDateTime endDateTime, OrderStatus orderStatus);


}
