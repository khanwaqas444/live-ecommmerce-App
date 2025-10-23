package com.livecommerce.product.service;

import com.livecommerce.product.domain.Product;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ProductService {
    Product createProduct(Product product, MultipartFile imageFile);
    Product updateProduct(Long id, Product product, MultipartFile imageFile);
    void deleteProduct(Long id);
    Product getProductById(Long id);
    List<Product> getAllProducts();
    List<Product> getProductsByCategory(String category);
    List<Product> searchProducts(String keyword);
    List<Product> getLiveProducts();
    void reduceStock(Long productId, Integer quantity);
}
