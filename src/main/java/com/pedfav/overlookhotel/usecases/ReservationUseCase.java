package com.pedfav.overlookhotel.usecases;

import com.pedfav.overlookhotel.entities.Reservation;
import com.pedfav.overlookhotel.exceptions.ResourceNotFoundException;
import com.pedfav.overlookhotel.gateway.repository.ReservationRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Component
@AllArgsConstructor
public class ReservationUseCase {

    private final ReservationRepository reservationRepository;

    public Reservation getReservationById(Long id) {
        return reservationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Reservation", "Id", id));
    }

    public Reservation placeReservation(Reservation reservation) {
        populateReservation(reservation);

        return reservationRepository.save(reservation);
    }

    public Reservation modifyReservation(Reservation reservation) {
        return null;
    }

    public Reservation cancelReservation(Long id) {
        return null;
    }

    private void populateReservation(Reservation reservation) {
        reservation.setStartDate(reservation.getStartDate().toLocalDate().atTime(LocalTime.MIN));
        reservation.setEndDate(reservation.getEndDate().toLocalDate().atTime(LocalTime.MAX));
        reservation.setInsertDate(LocalDateTime.now());
    }
}
