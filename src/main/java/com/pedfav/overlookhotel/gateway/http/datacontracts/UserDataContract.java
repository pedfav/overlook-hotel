package com.pedfav.overlookhotel.gateway.http.datacontracts;

import com.fasterxml.jackson.annotation.JsonInclude;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Past;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDataContract {

    private Long id;

    @NotBlank
    private String documentId;

    @NotBlank
    private String name;

    @Email(message = "Email should be valid")
    private String email;

    @Past
    private LocalDate birthday;
}