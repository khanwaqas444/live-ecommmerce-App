//package com.livecommerce.order.controller;
//
//import org.springframework.web.bind.annotation.*;
//import lombok.RequiredArgsConstructor;
//import com.livecommerce.order.repository.OrderRepository;
//import java.util.*;
//import java.util.UUID;
//
//@RestController
//@RequestMapping("/payments")
//@RequiredArgsConstructor
//public class PaymentsWebhookController {
//
//  private final OrderRepository orders;
//
//  record Webhook(String payment_id, String order_id, String status) {}
//
//  @PostMapping("/webhook")
//  public Map<String,Object> webhook(@RequestBody Webhook w) {
//    orders.findById(UUID.fromString(w.order_id()))
//            .ifPresent(o -> {
//              o.setStatus("succeeded".equals(w.status()) ? "paid" : "payment_failed");
//              orders.save(o);
//            });
//    return Map.of("received", true);
//  }
//}
