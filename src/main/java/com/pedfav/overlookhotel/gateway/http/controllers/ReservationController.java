package com.pedfav.overlookhotel.gateway.http.controllers;

import com.pedfav.overlookhotel.entities.Reservation;
import com.pedfav.overlookhotel.gateway.http.converters.CreateReserveConverter;
import com.pedfav.overlookhotel.gateway.http.converters.ReservationConverter;
import com.pedfav.overlookhotel.gateway.http.datacontracts.CreateReserveDataContract;
import com.pedfav.overlookhotel.gateway.http.datacontracts.ReservationDataContract;
import com.pedfav.overlookhotel.gateway.http.datacontracts.UserDataContract;
import com.pedfav.overlookhotel.usecases.ReservationUseCase;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@AllArgsConstructor
@RequestMapping(value = "/reservations")
public class ReservationController {

    private ReservationUseCase reservationUseCase;
    private ReservationConverter reservationConverter;
    private CreateReserveConverter createReserveConverter;

    @GetMapping("/{reservation_id}")
    public ResponseEntity<ReservationDataContract> getReservationById(@PathVariable("reservation_id") Long reservationId) {
        Reservation reservation = reservationUseCase.getReservationById(reservationId);

        return ResponseEntity.ok(reservationConverter.reservationToDataContract(reservation));
    }

    @PostMapping
    public ResponseEntity<ReservationDataContract> placeReservation(@Valid @RequestBody CreateReserveDataContract createReserve) {

        Reservation reservation = createReserveConverter.dataContractToReservation(createReserve);
        Reservation saved = reservationUseCase.placeReservation(reservation);

        return ResponseEntity.ok(reservationConverter.reservationToDataContract(saved));
    }

    @PatchMapping("/{reservation_id}")
    public ResponseEntity<UserDataContract> modifyReservation(@PathVariable("reservation_id") Long reservationId,
                                                              @Valid @RequestBody ReservationDataContract reservedDataContract) {
        return null;
    }

    @DeleteMapping("/{reservation_id}")
    public ResponseEntity<Void> cancelReservation(@PathVariable("reservation_id") Long reservationId) {
        return null;
    }

}
