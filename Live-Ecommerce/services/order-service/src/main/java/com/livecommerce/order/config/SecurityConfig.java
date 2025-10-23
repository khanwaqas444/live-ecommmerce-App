//package com.livecommerce.order.config;
//
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.PutMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//
//@FeignClient(name = "product-service", url = "http://localhost:8080/api/products")
//public interface ProductClient {
//
//  @PutMapping("/{id}/reduceStock")
//  void reduceStock(@PathVariable("id") String productId, @RequestParam("qty") Integer qty);
//}
