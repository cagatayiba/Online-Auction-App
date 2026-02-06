package com.cengo.muzayedebackendv2.domain.response.admin;

import com.cengo.muzayedebackendv2.domain.response.AssetResponse;

import java.time.LocalDateTime;
import java.util.UUID;

public record BlogAdminResponse(
        UUID id,
        String content,
        String name,
        String keywords,
        String metaDescription,
        String pageTitle,
        LocalDateTime createDate,
        AssetResponse coverImage
) {
}
