package com.pedfav.overlookhotel.gateway.http.datacontracts;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Past;
import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDataContract {

    private Long id;

    @NotBlank(message = "Name should not be empty")
    private String name;

    @Email(message = "Email should be valid")
    @NotBlank(message = "Email should not be empty")
    private String email;

    @JsonFormat(pattern = "dd/MM/yyyy")
    @Past(message = "Birthday must be in the past")
    private LocalDate birthday;
}