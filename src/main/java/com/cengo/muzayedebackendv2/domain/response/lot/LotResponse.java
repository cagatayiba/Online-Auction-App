package com.cengo.muzayedebackendv2.domain.response.lot;

import com.cengo.muzayedebackendv2.domain.response.AssetResponse;
import com.cengo.muzayedebackendv2.domain.response.artist.ArtistLotResponse;
import com.cengo.muzayedebackendv2.domain.response.bid.BidLotResponse;

import java.time.LocalDateTime;
import java.util.List;

public record LotResponse(
        Integer lotNumber,
        String name,
        String description,
        ArtistLotResponse artistInfo,
        AssetResponse coverImage,
        List<AssetResponse> assets,
        LocalDateTime saleEndTime,
        Boolean isBidPlaced,
        Boolean isWinner,
        Integer bidCount,
        Integer currentPrice,
        LotUserOfferResponse userActiveOffer,
        List<BidLotResponse> givenBids
) {
}
