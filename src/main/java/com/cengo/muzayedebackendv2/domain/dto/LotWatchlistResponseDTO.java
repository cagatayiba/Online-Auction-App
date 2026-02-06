package com.cengo.muzayedebackendv2.domain.dto;

import com.cengo.muzayedebackendv2.domain.response.AssetResponse;
import lombok.Builder;

import java.util.List;

@Builder
public record LotWatchlistResponseDTO(
        String artistName,
        AssetResponse coverImage,
        List<AssetResponse> assets,
        Boolean isBiddingEnabled,
        Boolean isCurrentAuction,
        Boolean isBidPlaced,
        Boolean isWinner,
        Integer bidCount,
        String bannerText
) {
}
