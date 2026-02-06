package com.cengo.muzayedebackendv2.domain.response.artist;

import com.cengo.muzayedebackendv2.domain.response.AssetResponse;

import java.util.List;

public record ArtistResponse(
        String fullName,
        String summary,
        String biography,
        AssetResponse profileImage,
        List<AssetResponse> workAssets,
        List<ArtistVideoResponse> videoUrls
) {
}
