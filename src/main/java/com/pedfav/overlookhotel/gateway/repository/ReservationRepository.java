package com.pedfav.overlookhotel.gateway.repository;

import com.pedfav.overlookhotel.entities.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    @Query(value = "SELECT * FROM reservations " +
            "WHERE start_date between :startDate AND :endDate " +
            "OR end_date between :startDate AND :endDate",
            nativeQuery = true)
    List<Reservation> findAvailability(LocalDateTime startDate, LocalDateTime endDate);

}