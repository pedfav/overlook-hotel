//package com.pedfav.overlookhotel.gateway.http.controllers;
//
//import com.pedfav.overlookhotel.gateway.http.datacontracts.ReservationDataContract;
//import com.pedfav.overlookhotel.usecases.ReservationUseCase;
//import lombok.AllArgsConstructor;
//import org.springframework.web.bind.annotation.*;
//
//import javax.websocket.server.PathParam;
//
//@AllArgsConstructor
//@RestController("/reservation")
//public class ReservationController {
//
//    private ReservationUseCase reservationUseCase;
//
//    @PostMapping
//    public void placeReservation(@RequestBody ReservationDataContract reservationDataContract) {
//
//    }
//
//    @PatchMapping("/{reservation-id}")
//    public void modifyReservation(@PathParam("reservation-id") String reservationId) {
//
//    }
//
//    @DeleteMapping("/{reservation-id}")
//    public void cancelReservation(@PathParam("reservation-id") String reservationId) {
//
//    }
//
//}
