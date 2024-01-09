package sample.coffeekiosk.unit;

import sample.coffeekiosk.unit.Beverage.Americano;
import sample.coffeekiosk.unit.Beverage.Latte;

import java.time.LocalDateTime;

public class CafeKioskRunner {

    public static void main(String[] args) {

        CafeKiosk cafeKiosk = new CafeKiosk();
        cafeKiosk.add(new Americano());
        System.out.println(">>> 아메리카노 추가");
        cafeKiosk.add(new Latte());
        System.out.println(">>> 라떼 추가");

        int totalPrice = cafeKiosk.calculateTotalPrice();
        System.out.println("총 주문가격 : " + totalPrice);
        System.out.println("cafeKiosk = " + cafeKiosk.getBeverages().size());

        Order order = cafeKiosk.createOrder(LocalDateTime.now());
        System.out.println("order.getBeverages() = " + order.getBeverages());

        cafeKiosk.clear();
        System.out.println("삭제 = " + cafeKiosk.getBeverages().size());


    }

}
