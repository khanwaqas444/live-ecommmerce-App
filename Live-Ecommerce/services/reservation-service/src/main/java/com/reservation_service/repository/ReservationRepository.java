package com.reservation_service.repository;

import com.reservation_service.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    List<Reservation> findByReservationDate(LocalDate date);

    @Query("SELECT r FROM Reservation r WHERE " +
            "LOWER(r.customerName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(r.customerPhone) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Reservation> search(String keyword);

    long countByReservationDate(LocalDate date);

    List<Reservation> findByStatus(String status);
}
