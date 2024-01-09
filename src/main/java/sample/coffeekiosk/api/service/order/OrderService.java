package sample.coffeekiosk.api.service.order;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import sample.coffeekiosk.api.controller.order.request.OrderCreateRequest;
import sample.coffeekiosk.api.service.order.response.OrderResponse;
import sample.coffeekiosk.domain.order.Order;
import sample.coffeekiosk.domain.order.OrderRepository;
import sample.coffeekiosk.domain.product.Product;
import sample.coffeekiosk.domain.product.ProductRepository;
import sample.coffeekiosk.domain.product.ProductType;
import sample.coffeekiosk.domain.stock.Stock;
import sample.coffeekiosk.domain.stock.StockRepository;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.*;

@RequiredArgsConstructor
@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final StockRepository stockRepository;

    /**
     * 재고 감소 -> 동시성 고민
     * optimistic lock / pessimistic lock / dirty read
     */
    public OrderResponse createOrder(OrderCreateRequest request, LocalDateTime registerDateTime) {
        List<String> productNumbers = request.getProductNumbers();
        List<Product> products = findProductsBy(productNumbers);

        // 재고 차감
        deductStockQuantities(products);

        Order order = Order.create(products, registerDateTime);
        Order saveOrder = orderRepository.save(order);

        return OrderResponse.of(saveOrder);
    }

    private void deductStockQuantities(List<Product> products) {
        // TODO 재고 차감 체크가 필요한 상품들 filter
        List<String> stockProductNumbers = extractStockProductNumbers(products);

        // TODO 재고 엔티티 조회
        Map<String, Stock> stockMap = createStockMapBy(stockProductNumbers);

        // TODO 상품별 counting
        Map<String, Long> productCountingMap = createCountingMapBy(stockProductNumbers);

        // TODO 재고 차감
        for ( String stockProductNumber : new HashSet<>(stockProductNumbers)) {
            Stock stock = stockMap.get(stockProductNumber);
            int quantity = productCountingMap.get(stockProductNumber).intValue();

            if ( stock.isQuantityLessThen(quantity) ) {
                throw new IllegalArgumentException("재고가 부족합니다.");
            }
            stock.deductQuantity(quantity);
        }
    }

    private static Map<String, Long> createCountingMapBy(List<String> stockProductNumbers) {
        return stockProductNumbers.stream()
                .collect(groupingBy(p -> p, Collectors.counting()));
    }

    private Map<String, Stock> createStockMapBy(List<String> stockProductNumbers) {
        List<Stock> allByProductNumberIn = stockRepository.findAllByProductNumberIn(stockProductNumbers);
        return allByProductNumberIn.stream()
                .collect(toMap(Stock::getProductNumber, Function.identity()));
    }

    private static List<String> extractStockProductNumbers(List<Product> products) {
        return products.stream()
                .filter(product -> ProductType.containsStockType(product.getType()))
                .map(Product::getProductNumber)
                .collect(toList());
    }

    // 아래 로직 검증 필요
    private List<Product> findProductsBy(List<String> productNumbers) {

        List<Product> products = productRepository.findAllByProductNumberIn(productNumbers);
//        Map<String, Product> productMap = products.stream()
//                .collect(toMap(Product::getProductNumber, Function.identity()));

        Map<String, Product> productMap = products.stream()
                .collect(toMap(Product::getProductNumber, Function.identity()));


        List<Product> collect = productNumbers.stream()
                .map(s -> productMap.get(s))
                .collect(toList());

        System.out.println("collect = " + collect);

        return collect;
    }

}
















//List<Product> products = productRepository.findAllByProductNumberIn(productNumbers);

//        products.stream()
//                .collect(Collectors.toMap(product -> product.getProductNumber(), p -> p ));

