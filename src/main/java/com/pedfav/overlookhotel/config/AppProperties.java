package com.pedfav.overlookhotel.config;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppProperties {

    @Value("${app.maxStayInDays:3}")
    private Integer maxStayInDays;

    @Value("${app.maxReserveInAdvance:30}")
    private Integer maxReserveInAdvance;

    @Value("${app.suggestionRange:5}")
    private Integer suggestionRange;

    @Bean
    public Properties maxStayInDays() {
        return new Properties(maxStayInDays, maxReserveInAdvance, suggestionRange);
    }
}
