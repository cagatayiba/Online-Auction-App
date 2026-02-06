package com.cengo.muzayedebackendv2.domain.response.lot;

import com.cengo.muzayedebackendv2.domain.response.AssetResponse;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Builder
public record LotAuctionResponse(
        UUID id,
        Integer lotNumber,
        String name,
        String description,
        String artistName,
        AssetResponse coverImage,
        List<AssetResponse> assets,
        LocalDateTime saleStartTime,
        LocalDateTime saleEndTime,
        Boolean isBiddingEnabled,
        Boolean isCurrentAuction,
        Boolean isBidPlaced,
        Boolean isWinner,
        Boolean isInWatchList,
        Integer bidCount,
        Integer currentPrice,
        String bannerText
) { }
