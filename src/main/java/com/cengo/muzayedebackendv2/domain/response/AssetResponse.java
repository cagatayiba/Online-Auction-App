package com.cengo.muzayedebackendv2.domain.response;

import com.cengo.muzayedebackendv2.domain.entity.enums.AssetMimeType;

import java.util.UUID;

public record AssetResponse(
        UUID id,
        String url,
        String alt,
        AssetMimeType type,
        Integer orderNo
) {
}
