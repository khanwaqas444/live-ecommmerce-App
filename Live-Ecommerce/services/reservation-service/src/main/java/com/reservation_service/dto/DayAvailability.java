package com.reservation_service.dto;

import lombok.Data;
import java.util.List;

@Data
public class DayAvailability {
    private boolean enabled;
    private String open;  // e.g., "10:00"
    private String close; // e.g., "20:00"
    private List<BreakPeriod> breaks;
}
