package com.livecommerce.product.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.livecommerce.product.domain.Product;
import com.livecommerce.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    // 🔹 Get all products / filter / search
    @GetMapping
    public ResponseEntity<List<Product>> getProducts(
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String search
    ) {
        if (category != null) {
            return ResponseEntity.ok(productService.getProductsByCategory(category));
        } else if (search != null) {
            return ResponseEntity.ok(productService.searchProducts(search));
        }
        return ResponseEntity.ok(productService.getAllProducts());
    }

    // 🔹 Get product by ID
    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable Long id) {
        return ResponseEntity.ok(productService.getProductById(id));
    }

    // 🔹 Get only live products
    @GetMapping("/live")
    public ResponseEntity<List<Product>> getLiveProducts() {
        return ResponseEntity.ok(productService.getLiveProducts());
    }

    // 🔹 Create product with image (multipart/form-data)
    @PostMapping(consumes = {"multipart/form-data"})
    public ResponseEntity<Product> createProduct(
            @RequestPart("product") String productJson,
            @RequestPart(value = "image", required = false) MultipartFile imageFile
    ) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            Product product = objectMapper.readValue(productJson, Product.class);
            Product savedProduct = productService.createProduct(product, imageFile);
            return ResponseEntity.ok(savedProduct);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    // 🔹 Update product with image
    @PutMapping(value = "/{id}", consumes = {"multipart/form-data"})
    public ResponseEntity<Product> updateProduct(
            @PathVariable Long id,
            @RequestPart("product") Product product,
            @RequestPart(value = "image", required = false) MultipartFile imageFile
    ) {
        return ResponseEntity.ok(productService.updateProduct(id, product, imageFile));
    }

    // 🔹 Delete product
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.ok("Product deleted successfully");
    }

    @PutMapping("/{productId}/reduce-stock")
    public ResponseEntity<Void> reduceStock(
            @PathVariable Long productId,
            @RequestParam Integer quantity) {

        productService.reduceStock(productId, quantity);
        return ResponseEntity.ok().build();
    }
}
