package com.cengo.muzayedebackendv2.domain.request;

import jakarta.validation.constraints.NotBlank;

public record UserUpdatePasswordRequest(
        @NotBlank String newPassword,
        @NotBlank String confirmPassword
) {
}
