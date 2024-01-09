package sample.coffeekiosk.domain.stock;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class StockTest {

    @DisplayName("재고를 주어진 개수 만큼 차감할 수 있다")
    @Test
    void Test() {

        // given
        Stock stock = Stock.builder()
                .productNumber("001")
                .quantity(2)
                .build();

        int quantity = 1;

        // when
        stock.deductQuantity(quantity);

        // then
        assertThat(stock.getQuantity()).isOne();
    }

    @DisplayName("재고 수량이 부족할 경우에 에러는 발생한다.")
    @Test
    void stockThrow() {

        // given
        Stock stock = Stock.create("001", 1);
       int quantity = 2;

       // when then
       assertThatThrownBy(() -> stock.deductQuantity(2))
               .isInstanceOf(IllegalArgumentException.class)
               .hasMessage("재고가 부족합니다.");
    }


}