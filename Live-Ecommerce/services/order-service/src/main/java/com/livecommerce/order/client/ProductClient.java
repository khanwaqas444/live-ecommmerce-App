package com.livecommerce.order.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.math.BigDecimal;
@FeignClient(name = "product-service", url = "http://localhost:8082")
public interface ProductClient {

    @GetMapping("/api/products/{id}")
    ProductDto getProductById(@PathVariable("id") Long id);

    @PutMapping("/api/products/{productId}/reduce-stock")
    void reduceStock(@PathVariable("productId") Long productId,
                     @RequestParam Integer quantity);

    class ProductDto {
        public Long id;
        public String name;
        public BigDecimal price;
        public Integer stock;
    }
}
