package com.compliance_service.service.impl;


import com.compliance_service.dto.KycRequest;
import com.compliance_service.dto.KycResponse;
import com.compliance_service.dto.KycVerifyRequest;
import com.compliance_service.entity.KycInfo;
import com.compliance_service.entity.KycStatus;
import com.compliance_service.mapper.KycMapper;
import com.compliance_service.repository.KycRepository;
import com.compliance_service.service.KycService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KycServiceImpl implements KycService {

    private final KycRepository kycRepository;

    @Override
    public KycResponse submitKyc(KycRequest request) {
        KycInfo kycInfo = kycRepository.findByUserId(request.getUserId())
                .orElse(new KycInfo());

        // Use mapper to populate entity
        KycMapper.toEntity(request, kycInfo);
        kycInfo.setStatus(KycStatus.PENDING);

        kycRepository.save(kycInfo);

        KycResponse response = KycMapper.toDto(kycInfo);
        response.setRemarks("KYC submitted and pending verification.");
        return response;
    }

    @Override
    public KycResponse getKycStatus(Long userId) {
        KycInfo kycInfo = kycRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("KYC not found for userId: " + userId));

        return KycMapper.toDto(kycInfo);
    }

    @Override
    public KycResponse verifyKyc(Long userId, KycVerifyRequest request) {
        KycInfo kycInfo = kycRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("KYC not found for userId: " + userId));

        if (request.isVerified()) {
            kycInfo.setStatus(KycStatus.VERIFIED);
            kycInfo.setRemarks("KYC verified successfully.");
        } else {
            kycInfo.setStatus(KycStatus.REJECTED);
            kycInfo.setRemarks(request.getRemarks() != null ? request.getRemarks() : "KYC rejected.");
        }

        kycRepository.save(kycInfo);

        return KycMapper.toDto(kycInfo);
    }
}