package sample.coffeekiosk.unit;


import lombok.Getter;
import sample.coffeekiosk.unit.Beverage.Beverage;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Getter
public class CafeKiosk {

    List<Beverage> beverages = new ArrayList<>();

    private final LocalTime SHOP_OPEN_TIME = LocalTime.of(10, 0);
    private final LocalTime SHOP_CLOSE_TIME = LocalTime.of(22, 0);

    // TODO 주문 추가
    public void add(Beverage beverage) {
        beverages.add(beverage);
    }

    // TODO 주문 추가
    public void add(Beverage beverage, int count) {

        if ( count <= 0 ) {
            throw new IllegalArgumentException("음료수 주문은 1개 이상 가능합니다.");
        }
        for ( int i=0; i<count; i++) {
            beverages.add(beverage);
        }
    }

    // TODO 주문 삭제
    public void remove(Beverage beverage) {
        beverages.remove(beverage);
    }

    // TODO 주문 전체 삭제
    public void clear() {
        beverages.clear();
    }

    // TODO 주문 전체 금액
    public int calculateTotalPrice() {
        int totalPrice = 0;
        if ( beverages.size() > 0 ) {
            for ( Beverage beverage : beverages ) {
                totalPrice += beverage.getPrice();
            }
        }
        return totalPrice;
    }

    // 전체 금액 계산 리팩토링
    public int calculateTotalPriceRefactoring() {
        int sum = beverages.stream()
                .mapToInt(Beverage::getPrice)
                .sum();

        System.out.println("sum = " + sum);

        return sum;

    }

    // TODO 주문 생성
    public Order createOrder(LocalDateTime currentDateTime) {

        LocalTime currentTime = currentDateTime.toLocalTime();
        if (currentTime.isBefore(SHOP_OPEN_TIME) || currentTime.isAfter(SHOP_CLOSE_TIME)) {
            throw new IllegalArgumentException("주문 가능 시간이 아닙니다.");
        }
        return new Order(LocalDateTime.now(), beverages);
    }
}
