package com.compliance_service.dto;


import com.compliance_service.entity.KycStatus;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class KycResponse {

    private Long userId;
    private String fullName;
    private String documentType;
    private KycStatus status;
    private String remarks;
}