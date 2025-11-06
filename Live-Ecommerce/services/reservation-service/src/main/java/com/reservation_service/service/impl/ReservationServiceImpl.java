package com.reservation_service.service.impl;

import com.reservation_service.controller.ReservationController.*;
import com.reservation_service.dto.*;
import com.reservation_service.entity.Reservation;
import com.reservation_service.repository.ReservationRepository;
import com.reservation_service.service.ReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReservationServiceImpl implements ReservationService {

    private final ReservationRepository reservationRepository;

    @Override
    public Reservation createReservation(Reservation reservation) {
        return reservationRepository.save(reservation);
    }

    @Override
    public Reservation createManualReservation(ManualReservationRequest req) {
        Reservation r = Reservation.builder()
                .customerName(req.getCustomerName())
                .customerPhone(req.getCustomerPhone())
                .customerEmail(req.getCustomerEmail())
                .reservationDate(req.getReservationDate())
                .timeSlot(req.getTimeSlot())
                .people(req.getPeople())
                .bookingSource(req.getBookingSource())
                .productIds(req.getProductIds())
                .specialRequests(req.getSpecialRequests())
                .status(req.isAutoConfirm() ? "CONFIRMED" : "PENDING")
                .build();
        return reservationRepository.save(r);
    }

    @Override
    public List<Reservation> listReservations(int page, int size, String status, String product,
                                              String sortBy, String dateRange,
                                              LocalDate startDate, LocalDate endDate) {
        List<Reservation> all = reservationRepository.findAll();
        if (status != null)
            all = all.stream().filter(r -> r.getStatus().equalsIgnoreCase(status)).collect(Collectors.toList());
        return all;
    }

    @Override
    public List<Reservation> searchReservations(String keyword) {
        return reservationRepository.search(keyword);
    }

    @Override
    public Map<String, Long> getReservationSummary() {
        Map<String, Long> summary = new LinkedHashMap<>();
        LocalDate today = LocalDate.now();
        summary.put("today", reservationRepository.countByReservationDate(today));
        summary.put("tomorrow", reservationRepository.countByReservationDate(today.plusDays(1)));
        summary.put("week", reservationRepository.findAll().stream()
                .filter(r -> !r.getReservationDate().isBefore(today) && !r.getReservationDate().isAfter(today.plusDays(7)))
                .count());
        summary.put("all", reservationRepository.count());
        return summary;
    }

    @Override
    public Reservation getReservationById(Long id) {
        return reservationRepository.findById(id).orElseThrow(() -> new RuntimeException("Reservation not found"));
    }

    @Override
    public Reservation updateReservation(Long id, Reservation reservation) {
        Reservation existing = getReservationById(id);
        reservation.setId(existing.getId());
        return reservationRepository.save(reservation);
    }

    @Override
    public Reservation updateStatus(Long id, String status) {
        Reservation r = getReservationById(id);
        r.setStatus(status.toUpperCase());
        return reservationRepository.save(r);
    }

    @Override
    public Reservation performAction(Long id, String action) {
        Reservation r = getReservationById(id);
        switch (action.toLowerCase()) {
            case "arrived" -> r.setStatus("ARRIVED");
            case "completed" -> r.setStatus("COMPLETED");
            case "no-show" -> r.setStatus("NO_SHOW");
        }
        return reservationRepository.save(r);
    }

    @Override
    public Reservation updateNotes(Long id, String notes) {
        Reservation r = getReservationById(id);
        r.setInternalNotes(notes);
        return reservationRepository.save(r);
    }

    @Override
    public List<Map<String, Object>> getCalendarEvents(LocalDate start, LocalDate end) {
        return reservationRepository.findAll().stream()
                .filter(r -> !r.getReservationDate().isBefore(start) && !r.getReservationDate().isAfter(end))
                .map(r -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("id", r.getId());
                    map.put("title", r.getCustomerName());
                    map.put("start", r.getReservationDate().toString());
                    map.put("status", r.getStatus());
                    return map;
                })
                .collect(Collectors.toList());
    }


    @Override
    public List<Map<String, Object>> getCalendarDaySummary(LocalDate monthStart, LocalDate monthEnd) {
        return reservationRepository.findAll().stream()
                .filter(r -> !r.getReservationDate().isBefore(monthStart) && !r.getReservationDate().isAfter(monthEnd))
                .collect(Collectors.groupingBy(Reservation::getReservationDate))
                .entrySet().stream()
                .map(e -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("date", e.getKey());
                    map.put("count", e.getValue().size());
                    return map;
                })
                .collect(Collectors.toList());
    }


    @Override
    public AvailabilityDto getAvailability() {
        AvailabilityDto dto = new AvailabilityDto();
        dto.setSlotDurationMinutes(30);
        dto.setCapacityPerSlot(2);
        return dto;
    }

    @Override
    public AvailabilityDto saveAvailability(AvailabilityDto availability) {
        return availability; // persist if needed
    }

    @Override
    public List<BlockedDateDto> getBlockedDates() {
        return new ArrayList<>();
    }

    @Override
    public BlockedDateDto addBlockedDate(BlockedDateDto dto) {
        return dto;
    }

    @Override
    public void deleteBlockedDate(String idOrDate) {}

    @Override
    public List<SlotDto> previewSlots(SlotPreviewRequest req) {
        List<SlotDto> slots = new ArrayList<>();
        slots.add(new SlotDto("10:00 AM", 2, false));
        slots.add(new SlotDto("10:30 AM", 1, false));
        return slots;
    }

    @Override
    public List<SlotDto> getSlotsForDate(LocalDate date) {
        return previewSlots(new SlotPreviewRequest());
    }

    @Override
    public void sendReminders(List<Long> reservationIds) {
        System.out.println("Reminders sent for: " + reservationIds);
    }

    @Override
    public String exportReservations(String format, String status, LocalDate startDate, LocalDate endDate) {
        return "http://localhost:8080/export/reservations.csv";
    }

    @Override
    public Map<String, Object> getAggregates(String by) {
        return Map.of("total", reservationRepository.count());
    }
}
