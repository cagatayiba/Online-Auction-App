package com.cengo.muzayedebackendv2.domain.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record SendVerificationDTO(
        @NotNull UUID id,
        @NotBlank String name,
        @NotBlank String email
) {
}
