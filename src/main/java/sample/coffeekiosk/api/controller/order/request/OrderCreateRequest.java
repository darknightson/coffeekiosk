package sample.coffeekiosk.api.controller.order.request;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import java.util.List;

@Data
@NoArgsConstructor
public class OrderCreateRequest {

    private List<String> productNumbers;

    @Email
    private String email;

    @Builder
    private OrderCreateRequest(List<String> productNumbers) {
        this.productNumbers = productNumbers;
    }
}
