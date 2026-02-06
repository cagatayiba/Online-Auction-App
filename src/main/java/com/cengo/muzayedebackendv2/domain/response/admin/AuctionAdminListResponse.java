package com.cengo.muzayedebackendv2.domain.response.admin;

import java.util.UUID;

public record AuctionAdminListResponse(
        UUID id,
        String name
) {
}
