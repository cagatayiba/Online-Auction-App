package com.cengo.muzayedebackendv2.domain.dto;

import com.cengo.muzayedebackendv2.domain.response.AssetResponse;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

@Builder
public record LotAuctionResponseDTO(
        String artistName,
        AssetResponse coverImage,
        List<AssetResponse> assets,
        Boolean isBiddingEnabled,
        Boolean isCurrentAuction,
        Boolean isBidPlaced,
        Boolean isWinner,
        Boolean isInWatchList,
        Integer bidCount,
        String bannerText,
        LocalDateTime saleStartTime
) {
}
