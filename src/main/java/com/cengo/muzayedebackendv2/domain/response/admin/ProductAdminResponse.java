package com.cengo.muzayedebackendv2.domain.response.admin;

import com.cengo.muzayedebackendv2.domain.entity.enums.ProductState;
import com.cengo.muzayedebackendv2.domain.response.AssetResponse;
import com.cengo.muzayedebackendv2.domain.response.user.UserBuyerResponse;

import java.util.List;
import java.util.UUID;

public record ProductAdminResponse(
        UUID id,
        String name,
        String description,
        Integer price,
        ProductState state,
        String category,
        UUID artistId,
        UserBuyerResponse requester,
        UserBuyerResponse buyer,
        String artistName,
        AssetResponse coverImage,
        List<AssetResponse> mediaAssets
) {
}
