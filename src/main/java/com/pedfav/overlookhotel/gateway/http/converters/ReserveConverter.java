package com.pedfav.overlookhotel.gateway.http.converters;

import com.pedfav.overlookhotel.entities.Reservation;
import com.pedfav.overlookhotel.entities.User;
import com.pedfav.overlookhotel.gateway.http.datacontracts.CreateReserveDataContract;
import com.pedfav.overlookhotel.gateway.http.datacontracts.ReservationDataContract;
import com.pedfav.overlookhotel.gateway.http.datacontracts.UpdateReserveDataContract;
import com.pedfav.overlookhotel.gateway.http.datacontracts.UserDataContract;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ReserveConverter {

    public Reservation createDataContractToReservation(CreateReserveDataContract dataContract) {
        return Reservation.builder()
                .user(User.builder()
                        .id(dataContract.getUserId())
                        .build())
                .startDate(dataContract.getStartDate().atTime(00, 00, 00))
                .endDate(dataContract.getEndDate().atTime(00, 00, 00))
                .build();
    }

    public ReservationDataContract reservationToReservationDataContract(Reservation reservation) {
        return ReservationDataContract.builder()
                .id(reservation.getId())
                .user(UserDataContract.builder()
                        .id(reservation.getUser().getId())
                        .name(reservation.getUser().getName())
                        .email(reservation.getUser().getEmail())
                        .birthday(reservation.getUser().getBirthday())
                        .build())
                .startDate(reservation.getStartDate())
                .endDate(reservation.getEndDate())
                .build();
    }

    public Reservation updateReserveDataContractToReservation(UpdateReserveDataContract dataContract) {
        return Reservation.builder()
                .startDate(dataContract.getStartDate().atTime(00, 00, 00))
                .endDate(dataContract.getEndDate().atTime(00, 00, 00))
                .build();
    }
}
