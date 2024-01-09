package sample.coffeekiosk.api.service.product;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sample.coffeekiosk.api.controller.product.dto.request.ProductCreateRequest;
import sample.coffeekiosk.api.service.product.response.ProductResponse;
import sample.coffeekiosk.domain.product.Product;
import sample.coffeekiosk.domain.product.ProductRepository;
import sample.coffeekiosk.domain.product.ProductSellingStatus;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    // 동시성 이슈
    // 증가 하는 값이라면 동시성 이슈가 발생할 수 있다.
    // 컬럼을 unique 하게 잡아서 동시성 이슈를 해결할 수 있다.
    // return 으로 받아서 재시도 요청을할 수 있다.
    @Transactional
    public ProductResponse createProduct(ProductCreateRequest request) {
        String latestProductNumber = productRepository.findLatestProductNumber().orElseGet(() -> "000");
        // String latestProductNumber 의 값에 + 1을 한다.
        String nextProductNumber = String.format("%03d", Integer.parseInt(latestProductNumber) + 1);

        Product product = request.toEntity(nextProductNumber);
        Product saveProduct = productRepository.save(product);
        return ProductResponse.of(saveProduct);
    }
    public List<ProductResponse> getSellingProducts() {
        List<Product> products = productRepository.findAllBySellingStatusIn(ProductSellingStatus.forDisplay());
        return products.stream()
                .map(ProductResponse::of)
                .collect(toList());
    }
}
