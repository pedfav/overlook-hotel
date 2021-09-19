package com.pedfav.overlookhotel.gateway.http.controllers;

import com.pedfav.overlookhotel.entities.Availability;
import com.pedfav.overlookhotel.entities.RoomAvailability;
import com.pedfav.overlookhotel.gateway.http.converters.AvailabilityConverter;
import com.pedfav.overlookhotel.gateway.http.datacontracts.PeriodAvailabilityDataContract;
import com.pedfav.overlookhotel.usecases.AvailabilityUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RequiredArgsConstructor
@RestController("/availability")
public class AvailabilityController {

    private final AvailabilityUseCase availabilityUseCase;
    private final AvailabilityConverter availabilityConverter;

    @GetMapping("/availability")
    public ResponseEntity<PeriodAvailabilityDataContract> getRoomAvailabilityByPeriod(@RequestParam("start_date") @DateTimeFormat(pattern = "dd/MM/yyyy") LocalDate startDate,
                                                                                      @RequestParam("end_date") @DateTimeFormat(pattern = "dd/MM/yyyy") LocalDate endDate) {

        Availability availability = availabilityUseCase.getRoomAvailabilityByPeriod(startDate, endDate);

        PeriodAvailabilityDataContract availabilityDataContract = availabilityConverter.availabilityToPeriodDataContract(availability);

        return ResponseEntity.ok(availabilityDataContract);
    }

    @GetMapping("/availability/month")
    public List<RoomAvailability> getRoomAvailabilityByMonth(@RequestParam("month") Integer month,
                                                             @RequestParam("year") Integer year) {

        return availabilityUseCase.getRoomAvailabilityByMonth(month, year);
    }
}

