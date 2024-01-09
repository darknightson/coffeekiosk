package sample.coffeekiosk.api.controller.product;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import sample.coffeekiosk.api.ApiResponse;
import sample.coffeekiosk.api.controller.product.dto.request.ProductCreateRequest;
import sample.coffeekiosk.api.service.product.ProductService;
import sample.coffeekiosk.api.service.product.response.ProductResponse;
import sample.coffeekiosk.handler.exception.CustomException;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @PostMapping("/api/v1/product/new")
    public ApiResponse<ProductResponse> createProduct(@Valid @RequestBody ProductCreateRequest request) {
        return ApiResponse.of(productService.createProduct(request));
    }

    @GetMapping("/api/v1/products/selling")
    public ApiResponse<List<ProductResponse>> getSellingProducts() {
        return ApiResponse.of(productService.getSellingProducts());
    }

    @GetMapping("/api/v1/products/test")
    public ApiResponse<Map<String, Object>> getTestProducts() {

        Map<String, Object> dataMap = new HashMap<String, Object>();
        dataMap.put("test", "name");

        return ApiResponse.of(dataMap);
    }

    @GetMapping("/api/v1/products/test/error")
    public ApiResponse<Map<String, Object>> getTestProductsCustomError() {

        throw new CustomException("테스트 에러");

    }
}
