package com.reservation_service.dto;

import lombok.Data;
import java.util.List;
import java.util.Map;

@Data
public class AvailabilityDto {
    // example fields
    private Map<String, DayAvailability> businessHours; // key=MON..SUN
    private int slotDurationMinutes;
    private int capacityPerSlot;
    private int bufferBetweenSlotsMinutes;
    private int advanceBookingDays;
    private boolean sameDayBookingAllowed;
    private List<BlockedDateDto> blockedDates;
}
