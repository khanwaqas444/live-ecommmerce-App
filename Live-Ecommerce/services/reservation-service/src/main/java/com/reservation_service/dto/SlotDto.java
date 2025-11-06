package com.reservation_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SlotDto {
    private String time;        // "10:00 AM - 10:30 AM"
    private int available;      // number of spots left
    private boolean full;
}