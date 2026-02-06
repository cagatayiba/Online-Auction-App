package com.cengo.muzayedebackendv2.domain.response.admin;

import com.cengo.muzayedebackendv2.domain.response.AssetResponse;

import java.util.UUID;

public record ArtistAdminListResponse(
        UUID id,
        String fullName,
        AssetResponse profileImage
) {
}
