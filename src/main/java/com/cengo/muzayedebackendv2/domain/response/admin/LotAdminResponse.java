package com.cengo.muzayedebackendv2.domain.response.admin;

import com.cengo.muzayedebackendv2.domain.entity.enums.LotState;
import com.cengo.muzayedebackendv2.domain.response.AssetResponse;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record LotAdminResponse(
        UUID id,
        String name,
        String description,
        Integer initialPrice,
        Integer finalPrice,
        LotState state,
        String artistName,
        UUID artistId,
        UUID auctionId,
        UUID buyerId,
        Integer lotNumber,
        LocalDateTime endTime,
        LocalDateTime extendedEndTime,
        UUID winnerOfferId,
        AssetResponse coverImage,
        List<AssetResponse> mediaAssets
) {
}
