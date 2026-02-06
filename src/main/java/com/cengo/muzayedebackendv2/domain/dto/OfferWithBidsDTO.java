package com.cengo.muzayedebackendv2.domain.dto;

import com.cengo.muzayedebackendv2.domain.entity.Bid;
import com.cengo.muzayedebackendv2.domain.entity.Offer;
import lombok.Builder;
import java.util.List;

@Builder
public record OfferWithBidsDTO(
        Offer offer,
        List<Bid> associatedBids
) {
}
