package com.cengo.muzayedebackendv2.domain.response.admin;

import com.cengo.muzayedebackendv2.domain.entity.enums.AuctionState;
import com.cengo.muzayedebackendv2.domain.response.AssetResponse;

import java.time.LocalDateTime;
import java.util.UUID;

public record AuctionAdminResponse(
        UUID id,
        String name,
        String description,
        LocalDateTime startTime,
        LocalDateTime endTime,
        AuctionState state,
        AssetResponse coverAsset
) {
}
