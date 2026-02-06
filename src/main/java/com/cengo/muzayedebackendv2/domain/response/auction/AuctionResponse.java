package com.cengo.muzayedebackendv2.domain.response.auction;

import com.cengo.muzayedebackendv2.domain.response.lot.LotAuctionResponse;
import org.springframework.data.domain.Page;


public record AuctionResponse(
        Page<LotAuctionResponse> lots
) {
}
