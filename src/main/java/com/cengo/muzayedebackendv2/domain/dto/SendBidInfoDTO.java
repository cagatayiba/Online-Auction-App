package com.cengo.muzayedebackendv2.domain.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.util.UUID;

@Builder
public record SendBidInfoDTO(
        @NotNull UUID lotId,
        @NotBlank String lotName,
        @NotBlank String userName,
        @NotNull Integer price,
        @NotBlank String email
) {
}
