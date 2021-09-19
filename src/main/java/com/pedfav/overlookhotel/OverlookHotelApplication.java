package com.pedfav.overlookhotel;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaRepositories
@SpringBootApplication
public class OverlookHotelApplication {

    public static void main(String[] args) {
        SpringApplication.run(OverlookHotelApplication.class, args);
    }

}
