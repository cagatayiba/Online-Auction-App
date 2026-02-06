package com.cengo.muzayedebackendv2.domain.response.bid;

import java.time.LocalDateTime;

public record BidLotResponse(
        String userFullName,
        LocalDateTime time,
        Integer price
) {
}
