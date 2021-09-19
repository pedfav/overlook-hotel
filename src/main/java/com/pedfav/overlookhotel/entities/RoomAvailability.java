package com.pedfav.overlookhotel.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoomAvailability {

    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate day;
    private Boolean available;
}
