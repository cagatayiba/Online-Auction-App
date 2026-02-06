package com.cengo.muzayedebackendv2.domain.response.lot;

import com.cengo.muzayedebackendv2.domain.response.AssetResponse;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record LotWatchlistResponse(
        UUID id,
        Integer lotNumber,
        String name,
        String description,
        String artistName,
        AssetResponse coverImage,
        List<AssetResponse> assets,
        LocalDateTime saleEndTime,
        Boolean isBiddingEnabled,
        Boolean isCurrentAuction,
        Boolean isBidPlaced,
        Boolean isWinner,
        Integer bidCount,
        Integer currentPrice,
        String bannerText
) {
}
