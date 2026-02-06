package com.cengo.muzayedebackendv2.domain.response.artist;

import java.util.UUID;

public record ArtistListAuctionResponse(
        UUID artistId,
        String artistFullName,
        Integer lotCount
) { }
