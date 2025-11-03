package com.reservation_service.service;

import com.reservation_service.entity.Reservation;
import com.reservation_service.repository.ReservationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.FileWriter;
import java.io.IOException;
import java.time.*;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationRepository reservationRepository;

    // ✅ Create Reservation
    public Reservation createReservation(Reservation reservation) {
        reservation.setBookingId("RES-" + UUID.randomUUID().toString().substring(0, 6).toUpperCase());
        reservation.setStatus(reservation.getStatus() == null ? "Pending" : reservation.getStatus());
        return reservationRepository.save(reservation);
    }

    // ✅ Get All
    public List<Reservation> getAllReservations() {
        return reservationRepository.findAll();
    }

    // ✅ Get by Date Range
    public List<Reservation> getReservationsByDate(LocalDateTime start, LocalDateTime end) {
        return reservationRepository.findByStartTimeBetween(start, end);
    }

    // ✅ Update Status
    public Reservation updateStatus(Long id, String status) {
        Reservation r = reservationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reservation not found with id: " + id));
        r.setStatus(status);
        return reservationRepository.save(r);
    }

    // ✅ Filter by Status, Product, and Sort
    public List<Reservation> filterReservations(String status, String product, String sortBy) {
        List<Reservation> all = reservationRepository.findAll();

        if (status != null && !status.isBlank())
            all = all.stream()
                    .filter(r -> r.getStatus().equalsIgnoreCase(status))
                    .collect(Collectors.toList());

        if (product != null && !product.isBlank())
            all = all.stream()
                    .filter(r -> r.getProductName().equalsIgnoreCase(product))
                    .collect(Collectors.toList());

        if (sortBy != null && sortBy.equalsIgnoreCase("date_desc"))
            all.sort(Comparator.comparing(Reservation::getStartTime).reversed());
        else if (sortBy != null && sortBy.equalsIgnoreCase("date_asc"))
            all.sort(Comparator.comparing(Reservation::getStartTime));

        return all;
    }

    // ✅ Search by keyword
    public List<Reservation> searchReservations(String keyword) {
        return reservationRepository.search(keyword);
    }

    // ✅ Summary counts for tabs (Today, Tomorrow, This Week, Past, All)
    public Map<String, Long> getReservationSummary() {
        LocalDate today = LocalDate.now();
        LocalDateTime startOfToday = today.atStartOfDay();
        LocalDateTime endOfToday = today.atTime(LocalTime.MAX);

        LocalDateTime startOfTomorrow = today.plusDays(1).atStartOfDay();
        LocalDateTime endOfTomorrow = today.plusDays(1).atTime(LocalTime.MAX);

        LocalDateTime startOfWeek = today.with(DayOfWeek.MONDAY).atStartOfDay();
        LocalDateTime endOfWeek = today.with(DayOfWeek.SUNDAY).atTime(LocalTime.MAX);

        long todayCount = reservationRepository.countByDateRange(startOfToday, endOfToday);
        long tomorrowCount = reservationRepository.countByDateRange(startOfTomorrow, endOfTomorrow);
        long weekCount = reservationRepository.countByDateRange(startOfWeek, endOfWeek);
        long pastCount = reservationRepository.findAll().stream()
                .filter(r -> r.getEndTime().isBefore(LocalDateTime.now()))
                .count();
        long allCount = reservationRepository.count();

        return Map.of(
                "today", todayCount,
                "tomorrow", tomorrowCount,
                "thisWeek", weekCount,
                "past", pastCount,
                "all", allCount
        );
    }

    // ✅ Calendar View
    public List<Map<String, Object>> getCalendarView() {
        return reservationRepository.findAll().stream()
                .map(r -> {
                    Map<String, Object> event = new HashMap<>();
                    event.put("title", r.getCustomerName() + " - " + r.getProductName());
                    event.put("start", r.getStartTime());
                    event.put("end", r.getEndTime());
                    event.put("status", r.getStatus());
                    return event;
                })
                .collect(Collectors.toList());
    }

    // ✅ Send Reminders (dummy)
    public void sendReminders(List<Long> reservationIds) {
        System.out.println("📩 Sending reminders for: " + reservationIds);
        // (Integrate Twilio, Mail, or push notification later)
    }

    // ✅ Export Reservations
    public String exportReservations(String format) {
        String fileName = "reservations_" + System.currentTimeMillis() + ".csv";
        try (FileWriter writer = new FileWriter(fileName)) {
            writer.append("Booking ID,Customer,Phone,Product,People,Status,Start Time,End Time\n");
            for (Reservation r : reservationRepository.findAll()) {
                writer.append(String.join(",",
                        r.getBookingId(),
                        r.getCustomerName(),
                        r.getCustomerPhone(),
                        r.getProductName(),
                        String.valueOf(r.getPeople()),
                        r.getStatus(),
                        String.valueOf(r.getStartTime()),
                        String.valueOf(r.getEndTime())
                )).append("\n");
            }
            return "File exported: " + fileName;
        } catch (IOException e) {
            throw new RuntimeException("Export failed", e);
        }
    }
}
