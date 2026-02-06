package com.cengo.muzayedebackendv2.domain.response.admin;


import com.cengo.muzayedebackendv2.domain.response.AssetResponse;

import java.util.UUID;

public record BlogAdminListResponse(
        UUID id,
        String name,
        String pageTitle,
        AssetResponse coverImage
) {
}
