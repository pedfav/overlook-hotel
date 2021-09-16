package com.pedfav.overlookhotel.gateway.http.controllers;

import com.pedfav.overlookhotel.gateway.http.datacontracts.MonthAvailabilityDataContract;
import com.pedfav.overlookhotel.gateway.http.datacontracts.PeriodAvailabilityDataContract;
import com.pedfav.overlookhotel.usecases.AvailabilityUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.websocket.server.PathParam;
import java.time.LocalDate;

@RequiredArgsConstructor
@RestController("/availability")
public class AvailabilityController {

    private AvailabilityUseCase availabilityUseCase;

    @GetMapping("/availability")
    public PeriodAvailabilityDataContract getRoomAvailabilityByPeriod(@PathParam("start_date") LocalDate startDate, @PathParam("end_date") LocalDate endDate) {
        return PeriodAvailabilityDataContract.builder().build();
    }

    @GetMapping("/availability/month/{month}")
    public MonthAvailabilityDataContract getRoomAvailabilityByMonth(@PathParam("month") Integer month) {
        return MonthAvailabilityDataContract.builder().build();
    }
}

