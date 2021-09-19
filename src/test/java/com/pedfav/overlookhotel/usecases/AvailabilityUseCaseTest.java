package com.pedfav.overlookhotel.usecases;

import com.pedfav.overlookhotel.config.Properties;
import com.pedfav.overlookhotel.entities.Availability;
import com.pedfav.overlookhotel.entities.Reservation;
import com.pedfav.overlookhotel.entities.RoomAvailability;
import com.pedfav.overlookhotel.exceptions.PlaceReservationException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static com.pedfav.overlookhotel.fixtures.ReservationFixture.reservation;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AvailabilityUseCaseTest {

    private final ReservationUseCase reservationUseCase = mock(ReservationUseCase.class);
    private final Properties properties = new Properties(3, 30, 5);

    private final AvailabilityUseCase availabilityUseCase = new AvailabilityUseCase(reservationUseCase, properties);

    @Test
    void testgetRoomAvailabilityByPeriodIsAvailable() {
        LocalDateTime starDate = LocalDateTime.now().plusDays(1);
        LocalDateTime endDate = LocalDateTime.now().plusDays(3);

        when(reservationUseCase.findAvailability(any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(Collections.emptyList());

        Availability roomAvailabilityByPeriod = availabilityUseCase.getRoomAvailabilityByPeriod(starDate.toLocalDate(), endDate.toLocalDate());

        verify(reservationUseCase, times(1)).findAvailability(any(LocalDateTime.class), any(LocalDateTime.class));
        assertTrue(roomAvailabilityByPeriod.getAvailable());
    }

    @Test
    void testgetRoomAvailabilityByPeriodPastDate() {
        LocalDateTime starDate = LocalDateTime.now().minusDays(1);
        LocalDateTime endDate = LocalDateTime.now().minusDays(3);

        assertThrows(PlaceReservationException.class,
                () -> availabilityUseCase.getRoomAvailabilityByPeriod(starDate.toLocalDate(), endDate.toLocalDate()),
                "Dates are in the past!");

        verify(reservationUseCase, times(0)).findAvailability(any(LocalDateTime.class), any(LocalDateTime.class));
    }

    @Test
    void testgetRoomAvailabilityByPeriodGreaterThanAllowed() {
        LocalDateTime starDate = LocalDateTime.now().plusDays(1);
        LocalDateTime endDate = LocalDateTime.now().plusDays(10);

        assertThrows(PlaceReservationException.class,
                () -> availabilityUseCase.getRoomAvailabilityByPeriod(starDate.toLocalDate(), endDate.toLocalDate()),
                "The period is greater than allowed!");

        verify(reservationUseCase, times(0)).findAvailability(any(LocalDateTime.class), any(LocalDateTime.class));
    }

    @Test
    void testGetRoomAvailabilityByMonthAllDaysAvailableSuccessfully() {
        int month = LocalDateTime.now().getMonthValue();
        int year = LocalDateTime.now().getYear();

        when(reservationUseCase.findAvailability(any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(Collections.emptyList());

        List<RoomAvailability> roomAvailabilityByMonth = availabilityUseCase.getRoomAvailabilityByMonth(month, year);

        verify(reservationUseCase, times(1)).findAvailability(any(LocalDateTime.class), any(LocalDateTime.class));
        assertTrue(roomAvailabilityByMonth.stream().allMatch(RoomAvailability::getAvailable));
    }

    @Test
    void testGetRoomAvailabilityByMonthRoomsTakenSuccessfully() {
        int month = LocalDateTime.now().getMonthValue();
        int year = LocalDateTime.now().getYear();

        Reservation reservationMock = reservation(LocalDateTime.now().plusDays(1), LocalDateTime.now().plusDays(1));

        when(reservationUseCase.findAvailability(any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(Collections.singletonList(reservationMock));

        List<RoomAvailability> roomAvailabilityByMonth = availabilityUseCase.getRoomAvailabilityByMonth(month, year);

        verify(reservationUseCase, times(1)).findAvailability(any(LocalDateTime.class), any(LocalDateTime.class));
        assertFalse(roomAvailabilityByMonth.stream()
                .allMatch(RoomAvailability::getAvailable));

        assertFalse(roomAvailabilityByMonth.stream()
                .filter(room -> room.getDay().equals(LocalDate.now().plusDays(1)))
                .findFirst()
                .get()
                .getAvailable());
    }

    @Test
    void testGetRoomAvailabilityByMonthPastDate() {
        int month = LocalDateTime.now().minusMonths(1).getMonthValue();
        int year = LocalDateTime.now().getYear();

        assertThrows(PlaceReservationException.class,
                () -> availabilityUseCase.getRoomAvailabilityByMonth(month, year),
                "Year and month must not be in the past!");

        verify(reservationUseCase, times(0)).findAvailability(any(LocalDateTime.class), any(LocalDateTime.class));

    }
}