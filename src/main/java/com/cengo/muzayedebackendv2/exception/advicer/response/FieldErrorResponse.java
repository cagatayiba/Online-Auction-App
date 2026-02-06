package com.cengo.muzayedebackendv2.exception.advicer.response;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

@Builder
public record FieldErrorResponse (
        @NotNull LocalDateTime timestamp,
        @NotNull List<String> errors,
        @NotNull String description,
        @NotNull int httpCode
) {
}


