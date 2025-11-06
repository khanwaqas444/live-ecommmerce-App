package com.reservation_service.service;

import com.reservation_service.controller.ReservationController.*;
import com.reservation_service.dto.*;
import com.reservation_service.entity.Reservation;

import java.time.LocalDate;
import java.util.*;

public interface ReservationService {

    Reservation createReservation(Reservation reservation);
    Reservation createManualReservation(ManualReservationRequest req);

    List<Reservation> listReservations(int page, int size, String status, String product, String sortBy,
                                       String dateRange, LocalDate startDate, LocalDate endDate);

    List<Reservation> searchReservations(String keyword);

    Map<String, Long> getReservationSummary();

    Reservation getReservationById(Long id);

    Reservation updateReservation(Long id, Reservation reservation);

    Reservation updateStatus(Long id, String status);

    Reservation performAction(Long id, String action);

    Reservation updateNotes(Long id, String notes);

    List<Map<String, Object>> getCalendarEvents(LocalDate start, LocalDate end);

    List<Map<String, Object>> getCalendarDaySummary(LocalDate monthStart, LocalDate monthEnd);

    AvailabilityDto getAvailability();

    AvailabilityDto saveAvailability(AvailabilityDto availability);

    List<BlockedDateDto> getBlockedDates();

    BlockedDateDto addBlockedDate(BlockedDateDto dto);

    void deleteBlockedDate(String idOrDate);

    List<SlotDto> previewSlots(SlotPreviewRequest req);

    List<SlotDto> getSlotsForDate(LocalDate date);

    void sendReminders(List<Long> reservationIds);

    String exportReservations(String format, String status, LocalDate startDate, LocalDate endDate);

    Map<String, Object> getAggregates(String by);
}
