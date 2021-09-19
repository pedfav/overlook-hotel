package com.pedfav.overlookhotel.config;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppProperties {

    @Value("${validations.maxStayInDays:3}")
    private Integer maxStayInDays;

    @Value("${validations.maxReserveInAdvance:30}")
    private Integer maxReserveInAdvance;

    @Bean
    public ValidationsProperties maxStayInDays() {
        return new ValidationsProperties(maxStayInDays, maxReserveInAdvance);
    }
}
