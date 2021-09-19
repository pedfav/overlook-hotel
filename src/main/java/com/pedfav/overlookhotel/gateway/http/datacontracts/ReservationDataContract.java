package com.pedfav.overlookhotel.gateway.http.datacontracts;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.validation.constraints.Future;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ReservationDataContract {

    private long id;

    @JsonProperty("user_id")
    private Long userId;

    @JsonProperty("user")
    private UserDataContract user;

    @Future
    @JsonProperty("start_date")
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
    private LocalDateTime startDate;

    @Future
    @JsonProperty("end_date")
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
    private LocalDateTime endDate;
}
