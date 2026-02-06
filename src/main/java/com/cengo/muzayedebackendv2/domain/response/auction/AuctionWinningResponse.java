package com.cengo.muzayedebackendv2.domain.response.auction;

import com.cengo.muzayedebackendv2.domain.response.AssetResponse;

import java.time.LocalDateTime;
import java.util.UUID;

public record AuctionWinningResponse(
        UUID id,
        String name,
        Integer auctionNo,
        LocalDateTime startTime,
        LocalDateTime endTime,
        AssetResponse coverAsset
) {
}
