package com.cengo.muzayedebackendv2.domain.dto;

import com.cengo.muzayedebackendv2.domain.response.AssetResponse;
import com.cengo.muzayedebackendv2.domain.response.artist.ArtistLotResponse;
import com.cengo.muzayedebackendv2.domain.response.bid.BidLotResponse;
import lombok.Builder;

import java.util.List;

@Builder
public record LotResponseDTO(
        ArtistLotResponse artistInfo,
        AssetResponse coverImage,
        List<AssetResponse> assets,
        Boolean isBidPlaced,
        Boolean isWinner,
        Integer bidCount,
        OfferWithBidsDTO userActiveOffer,
        List<BidLotResponse> givenBids
) {
}
