package com.cengo.muzayedebackendv2.domain.response.auction;

import com.cengo.muzayedebackendv2.domain.response.AssetResponse;

import java.time.LocalDateTime;
import java.util.UUID;

public record AuctionListResponse(
        UUID id,
        String name,
        String description,
        Integer auctionNo,
        LocalDateTime startTime,
        LocalDateTime endTime,
        AssetResponse coverAsset,
        Integer remainingTime,
        String infoText
) {
}
