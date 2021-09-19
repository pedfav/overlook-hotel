package com.pedfav.overlookhotel.usecases;

import com.pedfav.overlookhotel.config.Properties;
import com.pedfav.overlookhotel.entities.Reservation;
import com.pedfav.overlookhotel.exceptions.PlaceReservationException;
import com.pedfav.overlookhotel.exceptions.ResourceNotFoundException;
import com.pedfav.overlookhotel.gateway.repository.ReservationRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.pedfav.overlookhotel.fixtures.ReservationFixture.reservation;
import static com.pedfav.overlookhotel.fixtures.UserFixture.user;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReservationUseCaseTest {

    private final ReservationRepository reservationRepository = mock(ReservationRepository.class);
    private final UserUseCase userUseCase = mock(UserUseCase.class);
    private final Properties properties = new Properties(3, 30, 5);

    ReservationUseCase reservationUseCase = new ReservationUseCase(reservationRepository, userUseCase, properties);

    @Test
    void testGetUserByIdWhenItExists() {
        Reservation reservation = reservation(LocalDateTime.of(2021, 10, 10, 0, 0, 0),
                LocalDateTime.of(2021, 10, 11, 23, 59, 59));

        when(reservationRepository.findById(anyLong())).thenReturn(Optional.of(reservation));

        Reservation reservationById = reservationUseCase.getReservationById(1L);

        verify(reservationRepository, times(1)).findById(anyLong());
        assertEquals(reservationById, reservation);
    }

    @Test
    void testGetUserByIdWhenDoesntExists() {
        when(reservationRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> reservationUseCase.getReservationById(1L));
    }

    @Test
    void testPlaceReservationSuccessfully() {
        Reservation reservationMock = reservation(LocalDateTime.now().plusDays(5), LocalDateTime.now().plusDays(7));
        when(userUseCase.getUserById(anyLong())).thenReturn(user());
        when(reservationRepository.save(any(Reservation.class))).thenReturn(reservationMock);

        Reservation reservation = reservationUseCase.placeReservation(reservationMock);

        verify(reservationRepository, times(1)).save(any(Reservation.class));
        verify(userUseCase, times(1)).getUserById(anyLong());
        assertEquals(reservation, reservationMock);
    }

    @Test
    void testPlaceReservationStartsTodayException() {
        Reservation reservationMock = reservation(LocalDateTime.now(), LocalDateTime.now().plusDays(2));
        when(userUseCase.getUserById(anyLong())).thenReturn(user());
        when(reservationRepository.save(any(Reservation.class))).thenReturn(reservationMock);

        assertThrows(PlaceReservationException.class,
                () -> reservationUseCase.placeReservation(reservationMock),
                "Reserve needs to start at least the next day of booking!");

        verify(reservationRepository, times(0)).save(any(Reservation.class));
        verify(userUseCase, times(0)).getUserById(anyLong());
    }

    @Test
    void testPlaceReservationStartsTooFarException() {
        Reservation reservationMock = reservation(LocalDateTime.now().plusDays(45), LocalDateTime.now().plusDays(47));
        when(userUseCase.getUserById(anyLong())).thenReturn(user());
        when(reservationRepository.save(any(Reservation.class))).thenReturn(reservationMock);

        assertThrows(PlaceReservationException.class,
                () -> reservationUseCase.placeReservation(reservationMock),
                "Your reservation needs to start at least within 30 days!");

        verify(reservationRepository, times(0)).save(any(Reservation.class));
        verify(userUseCase, times(0)).getUserById(anyLong());
    }

    @Test
    void testPlaceReservationStartsDateAfterEndDateException() {
        Reservation reservationMock = reservation(LocalDateTime.now().plusDays(7), LocalDateTime.now().plusDays(5));
        when(userUseCase.getUserById(anyLong())).thenReturn(user());
        when(reservationRepository.save(any(Reservation.class))).thenReturn(reservationMock);

        assertThrows(PlaceReservationException.class,
                () -> reservationUseCase.placeReservation(reservationMock),
                "End date is before start date!");

        verify(reservationRepository, times(0)).save(any(Reservation.class));
        verify(userUseCase, times(0)).getUserById(anyLong());
    }

    @Test
    void testPlaceReservationLongerThanAllowedException() {
        Reservation reservationMock = reservation(LocalDateTime.now().plusDays(1), LocalDateTime.now().plusDays(5));
        when(userUseCase.getUserById(anyLong())).thenReturn(user());
        when(reservationRepository.save(any(Reservation.class))).thenReturn(reservationMock);

        assertThrows(PlaceReservationException.class,
                () -> reservationUseCase.placeReservation(reservationMock),
                "Your reservation is greater than allowed!");

        verify(reservationRepository, times(0)).save(any(Reservation.class));
        verify(userUseCase, times(0)).getUserById(anyLong());
    }

    @Test
    void testPlaceReservationUserDoesntExistsException() {
        Reservation reservationMock = reservation(LocalDateTime.now().plusDays(1), LocalDateTime.now().plusDays(2));
        when(userUseCase.getUserById(anyLong())).thenThrow(ResourceNotFoundException.class);
        when(reservationRepository.save(any(Reservation.class))).thenReturn(reservationMock);

        assertThrows(PlaceReservationException.class,
                () -> reservationUseCase.placeReservation(reservationMock),
                "User informed doesn't exists!");

        verify(reservationRepository, times(0)).save(any(Reservation.class));
        verify(userUseCase, times(1)).getUserById(anyLong());
    }

    @Test
    void testPlaceReservationAlreadyTakenException() {
        Reservation reservationMock = reservation(LocalDateTime.now().plusDays(1), LocalDateTime.now().plusDays(2));
        when(userUseCase.getUserById(anyLong())).thenReturn(user());
        when(reservationRepository.save(any(Reservation.class))).thenThrow(DataIntegrityViolationException.class);

        assertThrows(PlaceReservationException.class,
                () -> reservationUseCase.placeReservation(reservationMock),
                "Reservation period already taken!");

        verify(reservationRepository, times(1)).save(any(Reservation.class));
        verify(userUseCase, times(1)).getUserById(anyLong());
    }

    @Test
    void testModifyReservationSuccessfully() {
        Reservation reservationMock = reservation(LocalDateTime.now().plusDays(5), LocalDateTime.now().plusDays(7));
        when(reservationRepository.save(any(Reservation.class))).thenReturn(reservationMock);
        when(reservationRepository.findById(anyLong())).thenReturn(Optional.of(reservationMock));

        Reservation reservation = reservationUseCase.modifyReservation(reservationMock, 1L);

        verify(reservationRepository, times(1)).save(any(Reservation.class));
        verify(reservationRepository, times(1)).findById(anyLong());
        assertEquals(reservation, reservationMock);
    }

    @Test
    void testModifyReservationPeriodAlreadyTakenException() {
        Reservation reservationMock = reservation(LocalDateTime.now().plusDays(5), LocalDateTime.now().plusDays(7));
        when(reservationRepository.save(any(Reservation.class))).thenThrow(DataIntegrityViolationException.class);
        when(reservationRepository.findById(anyLong())).thenReturn(Optional.of(reservationMock));

        assertThrows(PlaceReservationException.class,
                () -> reservationUseCase.modifyReservation(reservationMock, 1L),
                "Reservation period already taken!");

        verify(reservationRepository, times(1)).save(any(Reservation.class));
        verify(reservationRepository, times(1)).findById(anyLong());
    }

    @Test
    void testDeleteReservationByIdWhenItExists() {
        Reservation reservationMock = reservation(LocalDateTime.now().plusDays(5), LocalDateTime.now().plusDays(7));
        when(reservationRepository.findById(anyLong())).thenReturn(Optional.of(reservationMock));

        reservationUseCase.cancelReservation(1L);

        verify(reservationRepository, times(1)).findById(anyLong());
        verify(reservationRepository, times(1)).deleteById(anyLong());
    }

    @Test
    void testDeleteReservationByIdWhenDoesntExists() {
        when(reservationRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> reservationUseCase.cancelReservation(1L));
    }

    @Test
    void testFindAvailabilityOfReservations() {
        Reservation reservationMock = reservation(LocalDateTime.now().plusDays(5), LocalDateTime.now().plusDays(7));
        when(reservationRepository.findAvailability(any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(Collections.singletonList(reservationMock));

        List<Reservation> availability = reservationUseCase.findAvailability(LocalDateTime.now(), LocalDateTime.now());

        verify(reservationRepository, times(1)).findAvailability(any(LocalDateTime.class), any(LocalDateTime.class));
        assertEquals(availability.size(), Collections.singletonList(reservationMock).size());
    }
}