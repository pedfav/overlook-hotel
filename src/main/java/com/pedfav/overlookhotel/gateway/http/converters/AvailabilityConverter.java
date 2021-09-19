package com.pedfav.overlookhotel.gateway.http.converters;

import com.pedfav.overlookhotel.entities.Availability;
import com.pedfav.overlookhotel.gateway.http.datacontracts.PeriodAvailabilityDataContract;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AvailabilityConverter {

    public PeriodAvailabilityDataContract availabilityToPeriodDataContract(Availability availability) {
        return PeriodAvailabilityDataContract.builder()
                .startDate(availability.getStartDate())
                .endDate(availability.getEndDate())
                .available(availability.getAvailable())
                .build();
    }

}
