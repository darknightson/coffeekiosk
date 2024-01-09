package sample.coffeekiosk.domain.product;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ProductTypeTest {


    @DisplayName("상품 타입이 재고 관련 타입인지 체크한다.")
    @Test
    void containStockTypeFalse() {

        // given
        ProductType productType = ProductType.HANDMADE;

        // when
        boolean result = ProductType.containsStockType(productType);

        // then
        assertThat(result).isFalse();
    }

    @DisplayName("상품 타입이 재고 관련 타입인지 체크한다.")
    @Test
    void ContainStockTypeTrue() {

        // given
        ProductType productType = ProductType.BOTTLE;

        // when
        boolean result = ProductType.containsStockType(productType);

        // then
        assertThat(result).isTrue();
    }

}