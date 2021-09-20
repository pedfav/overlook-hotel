package com.pedfav.overlookhotel.gateway.http.controllers;

import com.pedfav.overlookhotel.entities.Availability;
import com.pedfav.overlookhotel.entities.RoomAvailability;
import com.pedfav.overlookhotel.gateway.http.RestResponseEntityExceptionHandler;
import com.pedfav.overlookhotel.gateway.http.converters.AvailabilityConverter;
import com.pedfav.overlookhotel.usecases.AvailabilityUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
class AvailabilityControllerTest {

    @Mock
    private AvailabilityUseCase availabilityUseCase;

    @InjectMocks
    private AvailabilityController availabilityController;

    private final AvailabilityConverter availabilityConverter = new AvailabilityConverter();

    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        ReflectionTestUtils.setField(availabilityController, "availabilityConverter", availabilityConverter);

        mockMvc = MockMvcBuilders.standaloneSetup(availabilityController)
                .setControllerAdvice(new RestResponseEntityExceptionHandler())
                .build();
    }

    @Test
    void testGetAvailabilityByPeriodSuccessfully() throws Exception {
        when(availabilityUseCase.getRoomAvailabilityByPeriod(any(LocalDate.class), any(LocalDate.class)))
                .thenReturn(Availability.builder()
                        .startDate(LocalDate.now())
                        .endDate(LocalDate.now())
                        .available(true)
                        .build());

        mockMvc.perform(get("/availability")
                .param("start_date", "10/10/2021")
                .param("end_date", "10/10/2021")
                .contentType("application/json"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void testGetAvailabilityByMonthSuccessfully() throws Exception {
        when(availabilityUseCase.getRoomAvailabilityByMonth(any(Integer.class), any(Integer.class)))
                .thenReturn(Collections.singletonList(RoomAvailability.builder()
                        .day(LocalDate.now())
                        .available(true)
                        .build()));

        mockMvc.perform(get("/availability/month")
                .param("month", "10")
                .param("year", "2021")
                .contentType("application/json"))
                .andDo(print())
                .andExpect(status().isOk());
    }
}