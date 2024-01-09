package sample.coffeekiosk.domain.stock;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import sample.coffeekiosk.domain.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Getter
@Entity
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public class Stock extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String productNumber;

    private int quantity;

    @Builder
    private Stock(String productNumber, int quantity) {
        this.productNumber = productNumber;
        this.quantity = quantity;
    }

    public static Stock create(String productNumber, int quantity) {
        return Stock.builder()
                .productNumber(productNumber)
                .quantity(quantity)
                .build();
    }

    public void stockMinus(int quantity) {
        this.quantity -= quantity;
    }

    public boolean isQuantityLessThen(int quantity) {
        return this.quantity < quantity;
    }

    public void deductQuantity(int quantity) {
        if ( isQuantityLessThen(quantity) ) {
            throw new IllegalArgumentException("재고가 부족합니다.");
        }
        this.quantity -= quantity;
    }
}
