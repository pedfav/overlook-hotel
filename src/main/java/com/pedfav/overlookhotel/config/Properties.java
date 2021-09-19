package com.pedfav.overlookhotel.config;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Properties {

    private Integer maxStayInDays;
    private Integer maxReserveInAdvance;
    private Integer suggestionRange;

}
