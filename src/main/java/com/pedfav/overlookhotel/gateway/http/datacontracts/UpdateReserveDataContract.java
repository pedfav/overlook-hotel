package com.pedfav.overlookhotel.gateway.http.datacontracts;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.validation.constraints.Future;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UpdateReserveDataContract {

    @JsonProperty("start_date")
    @JsonFormat(pattern = "dd/MM/yyyy")
    @NotNull(message = "Start date must not be empty")
    @Future(message = "Start date must be a future date")
    private LocalDate startDate;

    @JsonProperty("end_date")
    @JsonFormat(pattern = "dd/MM/yyyy")
    @NotNull(message = "End date must not be empty")
    @Future(message = "End date must be a future date")
    private LocalDate endDate;
}
