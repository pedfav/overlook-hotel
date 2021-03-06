package com.pedfav.overlookhotel.usecases;

import com.pedfav.overlookhotel.config.Properties;
import com.pedfav.overlookhotel.entities.Reservation;
import com.pedfav.overlookhotel.exceptions.PlaceReservationException;
import com.pedfav.overlookhotel.exceptions.ResourceNotFoundException;
import com.pedfav.overlookhotel.gateway.repository.ReservationRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Slf4j
@Component
@AllArgsConstructor
public class ReservationUseCase {

    private final ReservationRepository reservationRepository;
    private final UserUseCase userUseCase;
    private final Properties properties;

    public Reservation getReservationById(Long id) {
        return reservationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Reservation", "Id", id));
    }

    public Reservation placeReservation(Reservation reservation) {

        try {
            Reservation reservationValidated = Optional.of(reservation)
                    .map(this::populateCreateReservation)
                    .map(this::checkIfReservationsStartsToday)
                    .map(this::checkReservationIsTooFar)
                    .map(this::checkEndDateBeforeStartDate)
                    .map(this::checkReservationLongerThanAllowed)
                    .map(this::checkUserExists)
                    .get();

            return reservationRepository.save(reservationValidated);

        } catch (ResourceNotFoundException e) {
            throw new PlaceReservationException("User informed doesn't exists!");
        } catch (DataIntegrityViolationException e) {
            throw new PlaceReservationException("Reservation period already taken!");
        }
    }

    public Reservation modifyReservation(Reservation reservation, Long id) {

        try {
            Reservation reservationValidated = Optional.of(reservation)
                    .map(res -> populateUpdateReservation(reservation, id))
                    .map(this::checkIfReservationsStartsToday)
                    .map(this::checkReservationIsTooFar)
                    .map(this::checkEndDateBeforeStartDate)
                    .map(this::checkReservationLongerThanAllowed)
                    .get();

            return reservationRepository.save(reservationValidated);
        } catch (DataIntegrityViolationException e) {
            throw new PlaceReservationException("Reservation period already taken!");
        }
    }

    public void cancelReservation(Long id) {
        reservationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Reservation", "Id", id));

        reservationRepository.deleteById(id);
    }

    public List<Reservation> findAvailability(LocalDateTime startDate, LocalDateTime endDate) {
        return reservationRepository.findAvailability(startDate, endDate);
    }

    private Reservation populateCreateReservation(Reservation reservation) {
        LocalDateTime now = LocalDateTime.now();
        reservation.setStartDate(reservation.getStartDate().toLocalDate().atStartOfDay());
        reservation.setEndDate(reservation.getEndDate().toLocalDate().atTime(23, 59, 59));
        reservation.setInsertDate(now);
        reservation.setUpdateDate(now);

        return reservation;
    }

    private Reservation populateUpdateReservation(Reservation reservation, Long id) {
        Reservation saved = getReservationById(id);

        LocalDateTime now = LocalDateTime.now();
        saved.setStartDate(reservation.getStartDate().toLocalDate().atStartOfDay());
        saved.setEndDate(reservation.getEndDate().toLocalDate().atTime(23, 59, 59));
        saved.setUpdateDate(now);

        return saved;
    }

    private Reservation checkIfReservationsStartsToday(Reservation reservation) {

        if (reservation.getStartDate().getDayOfMonth() == LocalDateTime.now().getDayOfMonth()) {
            throw new PlaceReservationException("Reserve needs to start at least the next day of booking!");
        }

        return reservation;
    }

    private Reservation checkReservationIsTooFar(Reservation reservation) {

        Duration daysToReserve = Duration.between(LocalDateTime.now(), reservation.getStartDate());

        if (daysToReserve.toDays() > properties.getMaxReserveInAdvance()) {
            throw new PlaceReservationException("Your reservation needs to start at least within 30 days!");
        }

        return reservation;
    }

    private Reservation checkEndDateBeforeStartDate(Reservation reservation) {
        Duration lengthOfStay = Duration.between(reservation.getStartDate(), reservation.getEndDate());

        if (lengthOfStay.toSeconds() < 0) {
            throw new PlaceReservationException("End date is before start date!");
        }

        return reservation;
    }

    private Reservation checkReservationLongerThanAllowed(Reservation reservation) {
        Duration lengthOfStay = Duration.between(reservation.getStartDate(), reservation.getEndDate());

        if ((lengthOfStay.toDays() + 1) > properties.getMaxStayInDays()) {
            throw new PlaceReservationException("Your reservation is greater than allowed!");
        }

        return reservation;
    }

    private Reservation checkUserExists(Reservation reservation) {
        userUseCase.getUserById(reservation.getUser().getId());

        return reservation;
    }
}
