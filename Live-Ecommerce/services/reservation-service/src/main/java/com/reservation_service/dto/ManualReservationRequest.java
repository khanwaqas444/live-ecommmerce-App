package com.reservation_service.dto;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.List;

@Data
public class ManualReservationRequest {
    // Customer details
    private String customerName;
    private String customerPhone;
    private String customerEmail;

    // Reservation details
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate reservationDate;
    private String timeSlot; // e.g. "10:00-10:30" or "10:00"
    private Integer people;
    private String bookingSource; // manual / walk-in / phone / stream
    private List<Long> productIds; // optional products of interest

    // Optional
    private String specialRequests;
    private boolean autoConfirm = true; // if manual should be auto-confirmed
}