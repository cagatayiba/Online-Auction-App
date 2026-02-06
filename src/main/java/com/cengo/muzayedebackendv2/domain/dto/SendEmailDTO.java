package com.cengo.muzayedebackendv2.domain.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record SendEmailDTO(
        @NotBlank String email,
        @NotBlank String subject,
        @NotBlank String htmlBody
) {
}
