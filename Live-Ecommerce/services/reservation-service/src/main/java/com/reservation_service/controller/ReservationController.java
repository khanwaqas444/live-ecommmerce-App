package com.reservation_service.controller;

import com.reservation_service.dto.*;
import com.reservation_service.entity.Reservation;
import com.reservation_service.service.ReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.*;

/**
 * Complete ReservationController covering:
 * - List / filter / search / summary (dashboard tabs)
 * - Manual reservation creation (Create Manual Reservation page)
 * - Reservation details, update status, quick actions (arrived/completed/no-show)
 * - Calendar API (events & dots per day)
 * - Availability endpoints: get/save availability, blocked dates, preview generated slots
 * - Reminders / export
 *
 * NOTE: This controller delegates heavy logic to ReservationService. Implement service methods accordingly.
 */
@RestController
@RequestMapping("/api/reservations")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class ReservationController {

    private final ReservationService reservationService;

    // ------------------------------
    // 1) Create reservation endpoints
    // ------------------------------

    /**
     * Create a regular reservation (used by app / automated flows).
     */
    @PostMapping
    public ResponseEntity<Reservation> create(@RequestBody Reservation reservation) {
        Reservation created = reservationService.createReservation(reservation);
        return ResponseEntity.ok(created);
    }

    /**
     * Create manual reservation (Create Manual Reservation page).
     * Manual reservations are often auto-confirmed depending on settings.
     */
    @PostMapping("/manual")
    public ResponseEntity<Reservation> createManual(@RequestBody ManualReservationRequest req) {
        Reservation created = reservationService.createManualReservation(req);
        return ResponseEntity.ok(created);
    }

    // ------------------------------
    // 2) List / filter / search / summary
    // ------------------------------

    /**
     * Paginated/list endpoint with optional filters.
     * Query params:
     *  - page, size (pagination)
     *  - status, product, sortBy
     *  - dateRange (today/tomorrow/week/past/custom)
     *  - startDate, endDate (ISO date strings, used for custom range)
     */
    @GetMapping
    public ResponseEntity<List<Reservation>> list(
            @RequestParam(required = false, defaultValue = "0") int page,
            @RequestParam(required = false, defaultValue = "50") int size,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String product,
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false) String dateRange,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate
    ) {
        List<Reservation> list = reservationService.listReservations(page, size, status, product, sortBy, dateRange, startDate, endDate);
        return ResponseEntity.ok(list);
    }

    /**
     * Search by keyword (name / phone / booking id).
     */
    @GetMapping("/search")
    public ResponseEntity<List<Reservation>> search(@RequestParam String keyword) {
        List<Reservation> results = reservationService.searchReservations(keyword);
        return ResponseEntity.ok(results);
    }

    /**
     * Summary counts for dashboard tabs (today / tomorrow / week / past / all).
     * Returns map with keys like "today","tomorrow","week","past","all".
     */
    @GetMapping("/summary")
    public ResponseEntity<Map<String, Long>> summary() {
        Map<String, Long> summary = reservationService.getReservationSummary();
        return ResponseEntity.ok(summary);
    }

    // ------------------------------
    // 3) Reservation details + updates
    // ------------------------------

    @GetMapping("/{id}")
    public ResponseEntity<Reservation> getById(@PathVariable Long id) {
        Reservation r = reservationService.getReservationById(id);
        return ResponseEntity.ok(r);
    }

    /**
     * Update reservation (full update).
     * For partial updates you can add separate endpoints.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Reservation> update(@PathVariable Long id, @RequestBody Reservation reservation) {
        Reservation updated = reservationService.updateReservation(id, reservation);
        return ResponseEntity.ok(updated);
    }

    /**
     * Update status: Confirmed, Pending, Cancelled, etc.
     */
    @PutMapping("/{id}/status")
    public ResponseEntity<Reservation> updateStatus(@PathVariable Long id, @RequestParam String status) {
        Reservation updated = reservationService.updateStatus(id, status);
        return ResponseEntity.ok(updated);
    }

    /**
     * Quick actions: arrived / completed / no-show
     * Example: PUT /{id}/action?action=arrived
     */
    @PutMapping("/{id}/action")
    public ResponseEntity<Reservation> action(@PathVariable Long id, @RequestParam String action) {
        Reservation updated = reservationService.performAction(id, action);
        return ResponseEntity.ok(updated);
    }

    /**
     * Add / update internal notes for a reservation (Create Manual Reservation page had an internal notes box).
     */
    @PutMapping("/{id}/notes")
    public ResponseEntity<Reservation> updateNotes(@PathVariable Long id, @RequestBody NotesRequest notesRequest) {
        Reservation updated = reservationService.updateNotes(id, notesRequest.getNotes());
        return ResponseEntity.ok(updated);
    }

    // ------------------------------
    // 4) Calendar view & dots (events)
    // ------------------------------

    /**
     * Return minimal calendar data for FullCalendar or custom calendar.
     * Suggested return per event: { id, title, start, end, status }
     */
    @GetMapping("/calendar")
    public ResponseEntity<List<Map<String, Object>>> calendar(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end
    ) {
        List<Map<String, Object>> events = reservationService.getCalendarEvents(start, end);
        return ResponseEntity.ok(events);
    }

    /**
     * Extra: return per-day summary used to draw dots (counts by status per date).
     * Example response: [{ date: '2025-01-08', counts: {confirmed:2, pending:1} }, ...]
     */
    @GetMapping("/calendar/summary")
    public ResponseEntity<List<Map<String, Object>>> calendarSummary(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate monthStart,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate monthEnd
    ) {
        List<Map<String, Object>> summary = reservationService.getCalendarDaySummary(monthStart, monthEnd);
        return ResponseEntity.ok(summary);
    }

    // ------------------------------
    // 5) Availability / slots / blocked dates
    // ------------------------------

    /**
     * Get current availability settings (business hours, slot duration, capacity, blocked dates, etc.)
     */
    @GetMapping("/availability")
    public ResponseEntity<AvailabilityDto> getAvailability() {
        AvailabilityDto dto = reservationService.getAvailability();
        return ResponseEntity.ok(dto);
    }

    /**
     * Save/update availability settings.
     */
    @PutMapping("/availability")
    public ResponseEntity<AvailabilityDto> saveAvailability(@RequestBody AvailabilityDto availability) {
        AvailabilityDto saved = reservationService.saveAvailability(availability);
        return ResponseEntity.ok(saved);
    }

    /**
     * Get blocked dates (holidays) list.
     */
    @GetMapping("/availability/blocked")
    public ResponseEntity<List<BlockedDateDto>> getBlockedDates() {
        List<BlockedDateDto> list = reservationService.getBlockedDates();
        return ResponseEntity.ok(list);
    }

    /**
     * Add a blocked date.
     */
    @PostMapping("/availability/blocked")
    public ResponseEntity<BlockedDateDto> addBlockedDate(@RequestBody BlockedDateDto dto) {
        BlockedDateDto added = reservationService.addBlockedDate(dto);
        return ResponseEntity.ok(added);
    }

    /**
     * Delete blocked date by id or by date string (support both).
     */
    @DeleteMapping("/availability/blocked/{idOrDate}")
    public ResponseEntity<Void> deleteBlockedDate(@PathVariable String idOrDate) {
        reservationService.deleteBlockedDate(idOrDate);
        return ResponseEntity.ok().build();
    }

    /**
     * Preview generated slots for a given date (used on Set Availability -> Preview Generated Slots / Create Manual Reservation -> available slots).
     * Request body can contain date and optional overrides (slotDuration, capacity).
     */
    @PostMapping("/availability/preview")
    public ResponseEntity<List<SlotDto>> previewSlots(@RequestBody SlotPreviewRequest req) {
        List<SlotDto> slots = reservationService.previewSlots(req);
        return ResponseEntity.ok(slots);
    }

    /**
     * Get available slots for a specific date (simple GET).
     */
    @GetMapping("/availability/slots")
    public ResponseEntity<List<SlotDto>> getSlotsForDate(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        List<SlotDto> slots = reservationService.getSlotsForDate(date);
        return ResponseEntity.ok(slots);
    }

    // ------------------------------
    // 6) Reminders / export / utilities
    // ------------------------------

    /**
     * Send reminders (bulk) - accepts reservation IDs array.
     */
    @PostMapping("/reminder")
    public ResponseEntity<String> sendReminders(@RequestBody List<Long> reservationIds) {
        reservationService.sendReminders(reservationIds);
        return ResponseEntity.ok("Reminders queued/sent");
    }

    /**
     * Export reservations (csv or excel). Returns URL or base64 link to generated file.
     */
    @GetMapping("/export")
    public ResponseEntity<String> export(@RequestParam(defaultValue = "csv") String format,
                                         @RequestParam(required = false) String status,
                                         @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
                                         @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        String fileUrl = reservationService.exportReservations(format, status, startDate, endDate);
        return ResponseEntity.ok(fileUrl);
    }

    // ------------------------------
    // 7) Misc / quick helpers
    // ------------------------------

    /**
     * Get counts by product or other aggregates (optional).
     */
    @GetMapping("/aggregates")
    public ResponseEntity<Map<String, Object>> aggregates(@RequestParam(required = false) String by) {
        Map<String, Object> res = reservationService.getAggregates(by);
        return ResponseEntity.ok(res);
    }

}
