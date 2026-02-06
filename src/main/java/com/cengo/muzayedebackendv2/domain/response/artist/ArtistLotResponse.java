package com.cengo.muzayedebackendv2.domain.response.artist;

import com.cengo.muzayedebackendv2.domain.response.AssetResponse;

import java.util.UUID;

public record ArtistLotResponse(
        UUID id,
        String fullName,
        String summary,
        AssetResponse profileImage
) {
}
