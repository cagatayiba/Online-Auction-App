package com.cengo.muzayedebackendv2.domain.message;

import lombok.Builder;

import java.util.UUID;

@Builder
public record NewOfferGivenMessage(
        UUID lotId,
        Integer lotPrice,
        UUID userId,
        Integer extraTime,
        Integer bidCount
) {
}
