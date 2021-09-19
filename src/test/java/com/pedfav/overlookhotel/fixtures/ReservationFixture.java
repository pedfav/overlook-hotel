package com.pedfav.overlookhotel.fixtures;

import com.pedfav.overlookhotel.entities.Reservation;

import java.time.LocalDateTime;

import static com.pedfav.overlookhotel.fixtures.UserFixture.user;

public class ReservationFixture {

    public static Reservation reservation(LocalDateTime startDate, LocalDateTime endDate) {
        return Reservation.builder()
                .id(1L)
                .user(user())
                .startDate(startDate)
                .endDate(endDate)
                .insertDate(LocalDateTime.now())
                .updateDate(LocalDateTime.now())
                .build();
    }
}
