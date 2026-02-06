package com.cengo.muzayedebackendv2.domain.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record UserUpdateRequest (
        @NotBlank @Email String email,
        @NotBlank String phone,
        @NotBlank String address
) {
}
