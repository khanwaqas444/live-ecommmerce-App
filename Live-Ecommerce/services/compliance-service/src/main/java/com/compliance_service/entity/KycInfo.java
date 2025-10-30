package com.compliance_service.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "kyc_info")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class KycInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    private String fullName;

    private String dob;

    @Enumerated(EnumType.STRING)
    private KycStatus status;  // UNVERIFIED, PENDING, VERIFIED, REJECTED

    private String documentType;       // e.g. AADHAAR, PAN, PASSPORT
    private String documentNumber;
    private String documentImageUrl;
    private String addressProofUrl;

    private String remarks;            // admin notes
}