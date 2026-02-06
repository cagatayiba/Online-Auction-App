package com.cengo.muzayedebackendv2.domain.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.util.UUID;

@Builder
public record SaveVideoDTO(
        @NotNull UUID artistId,
        @NotNull String header,
        @NotBlank String url,
        @NotNull Integer order
) {
}
