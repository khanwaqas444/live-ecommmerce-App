package com.compliance_service.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class KycRequest {

    @NotNull
    private Long userId;
    @NotBlank
    private String fullName;
    @NotBlank
    private String dob;
    @NotBlank
    private String documentType;
    @NotBlank
    private String documentNumber;
    private String documentImageUrl;
    private String addressProofUrl;
}