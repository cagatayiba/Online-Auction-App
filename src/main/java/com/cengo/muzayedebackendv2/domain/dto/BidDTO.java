package com.cengo.muzayedebackendv2.domain.dto;

import lombok.Builder;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
public record BidDTO(
        UUID offerId,
        UUID userId,
        LocalDateTime time,
        Integer price
) {
}
