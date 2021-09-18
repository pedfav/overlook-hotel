package com.pedfav.overlookhotel.gateway.http.converters;

import com.pedfav.overlookhotel.entities.Reservation;
import com.pedfav.overlookhotel.entities.User;
import com.pedfav.overlookhotel.gateway.http.datacontracts.CreateReserveDataContract;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CreateReserveConverter {

    public Reservation dataContractToReservation(CreateReserveDataContract dataContract) {
        return Reservation.builder()
                .user(User.builder()
                        .id(dataContract.getUserId())
                        .build())
                .startDate(dataContract.getStartDate().atTime(00, 00, 00))
                .endDate(dataContract.getEndDate().atTime(00, 00, 00))
                .build();
    }
}
