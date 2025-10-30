package com.compliance_service.service;

import com.compliance_service.dto.KycRequest;
import com.compliance_service.dto.KycResponse;
import com.compliance_service.dto.KycVerifyRequest;

public interface KycService {

    KycResponse submitKyc(KycRequest request);

    KycResponse getKycStatus(Long userId);

    KycResponse verifyKyc(Long userId, KycVerifyRequest request);
}