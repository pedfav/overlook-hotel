package com.pedfav.overlookhotel.config;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ValidationsProperties {

    private Integer maxStayInDays;
    private Integer maxReserveInAdvance;

}
