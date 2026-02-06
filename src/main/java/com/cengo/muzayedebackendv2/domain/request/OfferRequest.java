package com.cengo.muzayedebackendv2.domain.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.util.UUID;

public record OfferRequest(
        @NotNull UUID lotId,
        @NotNull @Positive Integer price
) {
}
