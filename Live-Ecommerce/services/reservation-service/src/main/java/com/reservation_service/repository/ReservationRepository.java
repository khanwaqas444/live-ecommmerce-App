package com.reservation_service.repository;

import com.reservation_service.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    List<Reservation> findByStartTimeBetween(LocalDateTime start, LocalDateTime end);
    List<Reservation> findByStatus(String status);
    List<Reservation> findByCustomerPhone(String customerPhone);

    // 🔍 Search by name, phone, or booking ID
    @Query("SELECT r FROM Reservation r WHERE LOWER(r.customerName) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(r.customerPhone) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(r.bookingId) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Reservation> search(@Param("keyword") String keyword);

    // 🧮 Count reservations between date ranges
    @Query("SELECT COUNT(r) FROM Reservation r WHERE r.startTime BETWEEN :start AND :end")
    Long countByDateRange(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);
}
