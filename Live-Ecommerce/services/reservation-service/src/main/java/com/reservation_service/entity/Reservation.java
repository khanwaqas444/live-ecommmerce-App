package com.reservation_service.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "reservations")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Unique booking reference (required by your DB)
    @Column(name = "booking_id", nullable = false, unique = true)
    private String bookingId;

    // -----------------------
    // Customer Information
    // -----------------------
    private String customerName;
    private String customerPhone;
    private String customerEmail;

    // -----------------------
    // Reservation Details
    // -----------------------
    private LocalDate reservationDate;
    private String timeSlot;
    private Integer people;

    private String status; // PENDING, CONFIRMED, CANCELLED, COMPLETED, NO_SHOW
    private String bookingSource; // manual / online / walk-in / phone
    private Double estimatedValue;

    // -----------------------
    // Linked Products (services/items)
    // -----------------------
    @ElementCollection
    @CollectionTable(
            name = "reservation_products",
            joinColumns = @JoinColumn(name = "reservation_id")
    )
    @Column(name = "product_id")
    private List<Long> productIds;

    // -----------------------
    // Optional Fields
    // -----------------------
    @Column(length = 2000)
    private String specialRequests;

    @Column(length = 2000)
    private String internalNotes;

    private boolean deleted = false;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // -----------------------
    // Auto Lifecycle Hooks
    // -----------------------
    @PrePersist
    public void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = createdAt;

        if (status == null || status.isBlank()) {
            status = "PENDING";
        }

        // Generate unique booking ID if missing
        if (bookingId == null || bookingId.isBlank()) {
            bookingId = "BK-" + System.currentTimeMillis();
        }
    }

    @PreUpdate
    public void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
