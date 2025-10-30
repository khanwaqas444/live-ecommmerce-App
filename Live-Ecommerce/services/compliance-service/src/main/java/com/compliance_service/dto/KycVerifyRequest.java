package com.compliance_service.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class KycVerifyRequest {

    private boolean verified;   // true = approve, false = reject
    private String remarks;
}