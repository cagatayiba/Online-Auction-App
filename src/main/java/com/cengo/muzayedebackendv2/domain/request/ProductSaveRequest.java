package com.cengo.muzayedebackendv2.domain.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.util.UUID;

public record ProductSaveRequest(
        @NotBlank String name,
        @NotBlank String description,
        @NotNull @Positive Integer price,
        @NotBlank String category,
        @NotNull UUID artistId
) {
}
