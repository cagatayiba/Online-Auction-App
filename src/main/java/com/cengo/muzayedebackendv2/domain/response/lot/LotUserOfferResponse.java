package com.cengo.muzayedebackendv2.domain.response.lot;

import java.time.LocalDateTime;
import java.util.List;

public record LotUserOfferResponse(
        Integer price,
        LocalDateTime time,
        List<LotOfferAssociatedBidsResponse> associatedBids
) {

    public record LotOfferAssociatedBidsResponse(
            Integer price,
            LocalDateTime time
    ){ }
}
