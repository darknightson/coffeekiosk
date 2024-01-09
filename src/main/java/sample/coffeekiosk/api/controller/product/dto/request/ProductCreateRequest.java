package sample.coffeekiosk.api.controller.product.dto.request;

import lombok.*;
import sample.coffeekiosk.domain.product.Product;
import sample.coffeekiosk.domain.product.ProductSellingStatus;
import sample.coffeekiosk.domain.product.ProductType;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Getter
@Setter
@NoArgsConstructor
public class ProductCreateRequest {

    @NotNull(message = "상품 타입은 필수입니다.")
    private ProductType type;

    @NotNull(message = "상품 판매 상태는 필수입니다.")
    private ProductSellingStatus sellingStatus;

    @NotBlank(message = "상품 이름은 필수입니다.")
    private String name;

    @Positive(message = "상품 가격은 0보다 커야합니다.") // 양수이여야 한다.
    private int price;

    @Builder
    public ProductCreateRequest(ProductType type, ProductSellingStatus sellingStatus, String name, int price) {
        this.type = type;
        this.sellingStatus = sellingStatus;
        this.name = name;
        this.price = price;
    }

    public Product toEntity(String nextProductNumber) {
            return Product.builder()
                    .productNumber(nextProductNumber)
                    .type(type)
                    .sellingStatus(sellingStatus)
                    .name(name)
                    .price(price)
                    .build();

    }
}
