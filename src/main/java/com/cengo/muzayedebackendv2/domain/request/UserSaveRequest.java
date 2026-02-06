package com.cengo.muzayedebackendv2.domain.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;

import java.time.LocalDate;

public record UserSaveRequest (
        @NotBlank String name,
        @NotBlank String surname,
        @NotBlank @Email String email,
        @NotBlank String password,
        @NotBlank String phone,
        @NotBlank String address,
        @NotNull @Past LocalDate birthDate,
        @NotNull Long idNumber
) {
}
