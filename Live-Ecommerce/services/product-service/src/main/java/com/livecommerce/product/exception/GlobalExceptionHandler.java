//package com.livecommerce.product.exception;
//
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.ExceptionHandler;
//import org.springframework.web.bind.annotation.RestControllerAdvice;
//import java.util.Map;
//
//@RestControllerAdvice
//public class GlobalExceptionHandler {
//
//    @ExceptionHandler(RuntimeException.class)
//    public ResponseEntity<Map<String, String>> handleRuntimeException(RuntimeException ex) {
//        return ResponseEntity.badRequest().body(Map.of("error", ex.getMessage()));
//    }
//}
