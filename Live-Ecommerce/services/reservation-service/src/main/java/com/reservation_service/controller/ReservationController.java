package com.reservation_service.controller;

import com.reservation_service.entity.Reservation;
import com.reservation_service.service.ReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/reservations")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class ReservationController {

    private final ReservationService reservationService;

    // ✅ Create a reservation
    @PostMapping
    public Reservation create(@RequestBody Reservation reservation) {
        return reservationService.createReservation(reservation);
    }

    // ✅ Get all reservations
    @GetMapping
    public List<Reservation> getAll() {
        return reservationService.getAllReservations();
    }

    // ✅ Get by date range (used for Today, Tomorrow, This Week, etc.)
    @GetMapping("/by-date")
    public List<Reservation> getByDate(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end
    ) {
        return reservationService.getReservationsByDate(start, end);
    }

    // ✅ Update reservation status (Confirm, Pending, Cancelled)
    @PutMapping("/{id}/status")
    public Reservation updateStatus(@PathVariable Long id, @RequestParam String status) {
        return reservationService.updateStatus(id, status);
    }

    // ✅ Filter by status, product, and sort order
    @GetMapping("/filter")
    public List<Reservation> filterReservations(
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String product,
            @RequestParam(required = false) String sortBy
    ) {
        return reservationService.filterReservations(status, product, sortBy);
    }

    // ✅ Search by customer name, phone, or booking ID
    @GetMapping("/search")
    public List<Reservation> searchReservations(@RequestParam String keyword) {
        return reservationService.searchReservations(keyword);
    }

    // ✅ Get summary counts (used for dashboard tabs like Today, Tomorrow, This Week, Past)
    @GetMapping("/summary")
    public Map<String, Long> getReservationSummary() {
        return reservationService.getReservationSummary();
    }

    // ✅ Calendar View API (returns minimal data for FullCalendar)
    @GetMapping("/calendar")
    public List<Map<String, Object>> getCalendarView() {
        return reservationService.getCalendarView();
    }

    // ✅ Bulk reminder endpoint
    @PostMapping("/reminder")
    public ResponseEntity<String> sendReminders(@RequestBody List<Long> reservationIds) {
        reservationService.sendReminders(reservationIds);
        return ResponseEntity.ok("Reminders sent successfully!");
    }

    // ✅ Export reservations as CSV or Excel
    @GetMapping("/export")
    public ResponseEntity<String> exportReservations(@RequestParam(defaultValue = "csv") String format) {
        String fileUrl = reservationService.exportReservations(format);
        return ResponseEntity.ok(fileUrl);
    }
}
