package com.compliance_service.controller;

import com.compliance_service.dto.KycRequest;
import com.compliance_service.dto.KycResponse;
import com.compliance_service.dto.KycVerifyRequest;
import com.compliance_service.service.KycService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/kyc")
@RequiredArgsConstructor
public class KycController {

    private final KycService kycService;

    // 1️⃣ Submit KYC
    @PostMapping("/submit")
    public ResponseEntity<KycResponse> submitKyc(@Valid @RequestBody KycRequest request) {
        KycResponse response = kycService.submitKyc(request);
        return ResponseEntity.ok(response);
    }

    // 2️⃣ Get KYC Status
    @GetMapping("/status/{userId}")
    public ResponseEntity<KycResponse> getKycStatus(@PathVariable Long userId) {
        KycResponse response = kycService.getKycStatus(userId);
        return ResponseEntity.ok(response);
    }

    // 3️⃣ Verify KYC (Admin)
    @PostMapping("/verify/{userId}")
    public ResponseEntity<KycResponse> verifyKyc(
            @PathVariable Long userId,
            @RequestBody KycVerifyRequest request) {
        KycResponse response = kycService.verifyKyc(userId, request);
        return ResponseEntity.ok(response);
    }
}