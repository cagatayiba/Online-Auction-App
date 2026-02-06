package com.cengo.muzayedebackendv2.domain.response.blog;

import com.cengo.muzayedebackendv2.domain.response.AssetResponse;

import java.time.LocalDateTime;
import java.util.UUID;

public record BlogResponse(
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
