package com.cengo.muzayedebackendv2.domain.response;

import java.util.List;

public record PriceOptionsResponse(
        List<Integer> prices
) {
}
