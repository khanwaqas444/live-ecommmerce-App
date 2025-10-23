//package com.livecommerce.order.controller;
//
//import org.springframework.web.bind.annotation.*;
//import lombok.RequiredArgsConstructor;
//import com.livecommerce.order.domain.Settlement;
//import com.livecommerce.order.repository.SettlementRepository;
//
//import java.time.OffsetDateTime;
//import java.util.*;
//import java.math.BigDecimal;
//import org.springframework.http.*;
//import org.springframework.security.access.prepost.PreAuthorize;
//
//@RestController
//@RequestMapping("/settlements")
//@RequiredArgsConstructor
//public class SettlementController {
//
//    private final SettlementRepository repo;
//
//    @GetMapping
//    public Map<String, Object> list() {
//        return Map.of("success", true, "data", repo.findAll());
//    }
//
//    @PostMapping("/generate")
//    public Map<String, Object> generate() {
//        Settlement s = Settlement.builder()
//                .sellerId("s1")
//                .amount(new BigDecimal("1000.00"))
//                .currency("INR")
//                .status("PENDING")
//                .createdAt(OffsetDateTime.now())
//                .build();
//        return Map.of("success", true, "data", repo.save(s));
//    }
//
//    @PreAuthorize("hasAnyAuthority('ADMIN')")
//    @PostMapping("/{id}/mark-paid")
//    public Map<String, Object> markPaid(@PathVariable UUID id) {
//        var s = repo.findById(id).orElseThrow();
//        s.setStatus("PAID");
//        return Map.of("success", true, "data", repo.save(s));
//    }
//
//    @PreAuthorize("hasAnyAuthority('ADMIN')")
//    @GetMapping(value = "/export", produces = "text/csv")
//    public ResponseEntity<String> exportCsv() {
//        var all = repo.findAll();
//        var sb = new StringBuilder("id,sellerId,amount,currency,status,createdAt\n");
//        for (var s : all) {
//            sb.append(s.getId()).append(',')
//                    .append(s.getSellerId()).append(',')
//                    .append(s.getAmount()).append(',')
//                    .append(s.getCurrency()).append(',')
//                    .append(s.getStatus()).append(',')
//                    .append(s.getCreatedAt()).append('\n');
//        }
//        return ResponseEntity.ok()
//                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=settlements.csv")
//                .body(sb.toString());
//    }
//}
