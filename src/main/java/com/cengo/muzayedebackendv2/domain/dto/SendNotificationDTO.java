package com.cengo.muzayedebackendv2.domain.dto;

import com.cengo.muzayedebackendv2.domain.enums.NotificationType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.util.UUID;

@Builder
public record SendNotificationDTO(
        @NotNull UUID userId,
        @NotNull NotificationType type,
        @NotBlank String productName,
        @NotBlank String link
) {
}
