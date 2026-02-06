package com.cengo.muzayedebackendv2.domain.response.artist;

import java.util.UUID;

public record ArtistListProductResponse(
        UUID artistId,
        String artistFullName,
        Integer productCount
) { }
