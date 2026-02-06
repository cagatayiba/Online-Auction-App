package com.cengo.muzayedebackendv2.domain.dto;

import com.cengo.muzayedebackendv2.domain.entity.Offer;
import lombok.Builder;

import java.util.List;

@Builder
public record OfferResultDTO(
        Offer winnerOffer,
        Integer updatedPrice,
        Boolean hasNewOfferWon,
        Boolean isBuyerReplaced,
        List<BidDTO> givenBids
) {}
