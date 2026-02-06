package com.cengo.muzayedebackendv2.domain.response.lot;

import com.cengo.muzayedebackendv2.domain.response.AssetResponse;
import com.cengo.muzayedebackendv2.domain.response.artist.ArtistLotResponse;

import java.util.List;

public record LotWinningResponse(
        String name,
        String description,
        ArtistLotResponse artistInfo,
        AssetResponse coverImage,
        List<AssetResponse> assets,
        Integer currentPrice
) {
}
