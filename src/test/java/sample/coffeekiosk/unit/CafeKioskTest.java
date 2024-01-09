package sample.coffeekiosk.unit;

import org.apache.tomcat.util.http.fileupload.MultipartStream;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import sample.coffeekiosk.unit.Beverage.Americano;
import sample.coffeekiosk.unit.Beverage.Beverage;
import sample.coffeekiosk.unit.Beverage.Latte;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

class CafeKioskTest {

    @Test
    void add_manual_test() {
        CafeKiosk cafeKiosk = new CafeKiosk();
        cafeKiosk.add(new Americano());

        System.out.println(">>> 담긴 음료 수 : " + cafeKiosk.getBeverages().size());
        System.out.println(">>> 담긴 음료 : " + cafeKiosk.getBeverages().get(0).getName());
    }

    @Test
    void add() {
        CafeKiosk cafeKiosk = new CafeKiosk();
        cafeKiosk.add(new Americano());

        assertThat(cafeKiosk.getBeverages().size()).isEqualTo(1);
        assertThat(cafeKiosk.getBeverages().get(0).getName()).isEqualTo("Americano");
    }

    @Test
    void add_several_cups() {
        CafeKiosk cafeKiosk = new CafeKiosk();
        Beverage beverage = new Americano();
        cafeKiosk.add(beverage, 2);

        assertThat(cafeKiosk.getBeverages().size()).isEqualTo(2);
        assertThat(cafeKiosk.getBeverages().get(0)).isEqualTo(beverage);
        assertThat(cafeKiosk.getBeverages().get(1)).isEqualTo(beverage);
    }

    @Test
    void add_several_cups_zero() {
        CafeKiosk cafeKiosk = new CafeKiosk();
        Beverage beverage = new Americano();

        assertThatThrownBy(() -> cafeKiosk.add(beverage, 0))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("음료수 주문은 1개 이상 가능합니다.");
    }


    @Test
    void remove() {
        CafeKiosk cafeKiosk = new CafeKiosk();
        Americano americano = new Americano();

        cafeKiosk.add(americano);
        assertThat(cafeKiosk.getBeverages()).hasSize(1);

        cafeKiosk.remove(americano);
        assertThat(cafeKiosk.getBeverages()).isEmpty();
    }

    @Test
    void clear() {
        CafeKiosk cafeKiosk = new CafeKiosk();
        Americano americano = new Americano();
        Latte latte = new Latte();

        cafeKiosk.add(americano);
        cafeKiosk.add(latte);
        assertThat(cafeKiosk.getBeverages()).hasSize(2);

        cafeKiosk.clear();
        assertThat(cafeKiosk.getBeverages()).isEmpty();
    }

    @Test
    void calculateTotalPriceTest() {
        CafeKiosk cafeKiosk = new CafeKiosk();
        Americano americano = new Americano();
        Latte latte = new Latte();

        cafeKiosk.add(americano);
        cafeKiosk.add(latte);

        int totalPrice = cafeKiosk.calculateTotalPriceRefactoring();

        assertThat(totalPrice).isEqualTo(8500);

    }

    @Test
    void createOrderWithCurrentTime() {
        CafeKiosk cafeKiosk = new CafeKiosk();
        Americano americano = new Americano();

        cafeKiosk.add(americano,2);

        Order order = cafeKiosk.createOrder(LocalDateTime.of(2023, 7, 3, 10, 0, 0));

        assertThat(order.getBeverages()).hasSize(2);
        assertThat(order.getBeverages().get(0).getName()).isEqualTo("Americano");

    }

    @Test
    void createOrderOutsideOpenTime() {
        CafeKiosk cafeKiosk = new CafeKiosk();
        Americano americano = new Americano();

        cafeKiosk.add(americano,2);

        assertThatThrownBy(() -> cafeKiosk.createOrder(LocalDateTime.of(2023, 7, 3, 9, 59, 0)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("주문 가능 시간이 아닙니다.");

    }


}