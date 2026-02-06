package com.cengo.muzayedebackendv2.domain.response.product;

import com.cengo.muzayedebackendv2.domain.entity.enums.ProductState;
import com.cengo.muzayedebackendv2.domain.response.AssetResponse;

import java.util.List;
import java.util.UUID;

public record ProductOrderResponse(
        UUID id,
        String name,
        String description,
        Integer price,
        ProductState state,
        String category,
        UUID artistId,
        String artistName,
        AssetResponse coverImage,
        List<AssetResponse> mediaAssets
) {
}
