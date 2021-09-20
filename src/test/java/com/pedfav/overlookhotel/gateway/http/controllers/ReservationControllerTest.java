package com.pedfav.overlookhotel.gateway.http.controllers;

import com.pedfav.overlookhotel.entities.Reservation;
import com.pedfav.overlookhotel.exceptions.PlaceReservationException;
import com.pedfav.overlookhotel.gateway.http.RestResponseEntityExceptionHandler;
import com.pedfav.overlookhotel.gateway.http.converters.ReserveConverter;
import com.pedfav.overlookhotel.usecases.ReservationUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;

import static com.pedfav.overlookhotel.fixtures.ReservationFixture.reservation;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
class ReservationControllerTest {

    @Mock
    private ReservationUseCase reservationUseCase;

    @InjectMocks
    private ReservationController reservationController;

    private final ReserveConverter reserveConverter = new ReserveConverter();

    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        ReflectionTestUtils.setField(reservationController, "reserveConverter", reserveConverter);

        mockMvc = MockMvcBuilders.standaloneSetup(reservationController)
                .setControllerAdvice(new RestResponseEntityExceptionHandler())
                .build();
    }

    @Test
    void testGetReservationByIdSuccessfully() throws Exception {
        Reservation reservation = reservation(LocalDateTime.now().plusDays(1), LocalDateTime.now().plusDays(2));
        when(reservationUseCase.getReservationById(any())).thenReturn(reservation);

        mockMvc.perform(get("/reservations/1")
                .contentType("application/json"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(reservation.getId()));
    }

    @Test
    void testCreateReservation() throws Exception {
        Reservation reservation = reservation(LocalDateTime.now().plusDays(1), LocalDateTime.now().plusDays(2));
        when(reservationUseCase.placeReservation(any(Reservation.class))).thenReturn(reservation);
        String content = "{ \"user_id\": 1, \"start_date\": \"20/09/2021\", \"end_date\": \"22/09/2021\" }";

        mockMvc.perform(post("/reservations")
                .contentType("application/json")
                .content(content))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void testCreateReservationPlaceReservationException() throws Exception {
        when(reservationUseCase.placeReservation(any(Reservation.class))).thenThrow(PlaceReservationException.class);
        String content = "{ \"user_id\": 1, \"start_date\": \"20/09/2021\", \"end_date\": \"22/09/2021\" }";

        mockMvc.perform(post("/reservations")
                .contentType("application/json")
                .content(content))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void testCreateReservationMissingField() throws Exception {
        Reservation reservation = reservation(LocalDateTime.now().plusDays(1), LocalDateTime.now().plusDays(2));
        when(reservationUseCase.placeReservation(any(Reservation.class))).thenReturn(reservation);
        String content = "{ \"start_date\": \"20/09/2021\", \"end_date\": \"22/09/2021\" }";

        mockMvc.perform(post("/reservations")
                .contentType("application/json")
                .content(content))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void testCreateReservationPastDate() throws Exception {
        Reservation reservation = reservation(LocalDateTime.now().plusDays(1), LocalDateTime.now().plusDays(2));
        when(reservationUseCase.placeReservation(any(Reservation.class))).thenReturn(reservation);
        String content = "{ \"start_date\": \"20/09/2020\", \"end_date\": \"22/09/2021\" }";

        mockMvc.perform(post("/reservations")
                .contentType("application/json")
                .content(content))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void testModifyReservation() throws Exception {
        Reservation reservation = reservation(LocalDateTime.now().plusDays(1), LocalDateTime.now().plusDays(2));
        when(reservationUseCase.modifyReservation(any(Reservation.class), anyLong())).thenReturn(reservation);
        String content = "{ \"start_date\": \"30/11/2021\", \"end_date\": \"03/11/2021\" }";

        mockMvc.perform(patch("/reservations/1")
                .contentType("application/json")
                .content(content))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void testDeleteReservationByIdSuccessfully() throws Exception {
        mockMvc.perform(delete("/reservations/1")
                .contentType("application/json"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void testDeleteReservationByIdRunTimeException() throws Exception {
        doThrow(RuntimeException.class).when(reservationUseCase).cancelReservation(anyLong());

        mockMvc.perform(delete("/reservations/1")
                .contentType("application/json"))
                .andDo(print())
                .andExpect(status().isInternalServerError());
    }
}