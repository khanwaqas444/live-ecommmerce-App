package com.compliance_service.mapper;

import com.compliance_service.dto.KycRequest;
import com.compliance_service.dto.KycResponse;
import com.compliance_service.entity.KycInfo;

public class KycMapper {

    // Convert KycRequest to KycInfo (for submit)
    public static KycInfo toEntity(KycRequest request, KycInfo existing) {
        if (existing == null) {
            existing = new KycInfo();
        }

        existing.setUserId(request.getUserId());
        existing.setFullName(request.getFullName());
        existing.setDob(request.getDob());
        existing.setDocumentType(request.getDocumentType());
        existing.setDocumentNumber(request.getDocumentNumber());
        existing.setDocumentImageUrl(request.getDocumentImageUrl());
        existing.setAddressProofUrl(request.getAddressProofUrl());

        return existing;
    }

    // Convert KycInfo to KycResponse
    public static KycResponse toDto(KycInfo entity) {
        KycResponse response = new KycResponse();
        response.setUserId(entity.getUserId());
        response.setFullName(entity.getFullName());
        response.setDocumentType(entity.getDocumentType());
        response.setStatus(entity.getStatus());
        response.setRemarks(entity.getRemarks());
        return response;
    }
}