package com.pedfav.overlookhotel.gateway.http.controllers;

import com.pedfav.overlookhotel.entities.Reservation;
import com.pedfav.overlookhotel.gateway.http.converters.ReserveConverter;
import com.pedfav.overlookhotel.gateway.http.datacontracts.CreateReserveDataContract;
import com.pedfav.overlookhotel.gateway.http.datacontracts.ReservationDataContract;
import com.pedfav.overlookhotel.gateway.http.datacontracts.UpdateReserveDataContract;
import com.pedfav.overlookhotel.usecases.ReservationUseCase;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@AllArgsConstructor
@RequestMapping(value = "/reservations")
public class ReservationController {

    private final ReservationUseCase reservationUseCase;
    private final ReserveConverter reserveConverter;

    @GetMapping("/{reservation_id}")
    public ResponseEntity<ReservationDataContract> getReservationById(@PathVariable("reservation_id") Long id) {
        Reservation reservation = reservationUseCase.getReservationById(id);

        return ResponseEntity.ok(reserveConverter.reservationToReservationDataContract(reservation));
    }

    @PostMapping
    public ResponseEntity<ReservationDataContract> placeReservation(@Valid @RequestBody CreateReserveDataContract dataContract) {

        Reservation reservation = reserveConverter.createDataContractToReservation(dataContract);
        Reservation saved = reservationUseCase.placeReservation(reservation);

        return ResponseEntity.ok(reserveConverter.reservationToReservationDataContract(saved));
    }

    @PatchMapping("/{reservation_id}")
    public ResponseEntity<ReservationDataContract> modifyReservation(@PathVariable("reservation_id") Long id,
                                                                     @Valid @RequestBody UpdateReserveDataContract dataContract) {

        Reservation reservation = reserveConverter.updateReserveDataContractToReservation(dataContract);
        Reservation modifiedReservation = reservationUseCase.modifyReservation(reservation, id);

        return ResponseEntity.ok(reserveConverter.reservationToReservationDataContract(modifiedReservation));
    }

    @DeleteMapping("/{reservation_id}")
    public ResponseEntity<Void> cancelReservation(@PathVariable("reservation_id") Long id) {
        reservationUseCase.cancelReservation(id);

        return ResponseEntity.ok().build();
    }
}
