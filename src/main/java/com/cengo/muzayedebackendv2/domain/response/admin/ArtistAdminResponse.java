package com.cengo.muzayedebackendv2.domain.response.admin;

import com.cengo.muzayedebackendv2.domain.response.artist.ArtistVideoResponse;
import com.cengo.muzayedebackendv2.domain.response.AssetResponse;

import java.util.List;
import java.util.UUID;

public record ArtistAdminResponse(
        UUID id,
        String fullName,
        String summary,
        String biography,
        AssetResponse profileImage,
        List<AssetResponse> workAssets,
        List<ArtistVideoResponse> videoUrls
) {
}
