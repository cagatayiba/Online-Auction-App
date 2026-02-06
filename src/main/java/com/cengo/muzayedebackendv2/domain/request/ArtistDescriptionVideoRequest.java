package com.cengo.muzayedebackendv2.domain.request;

import jakarta.validation.constraints.NotBlank;

public record ArtistDescriptionVideoRequest(
        @NotBlank String header,
        @NotBlank String url
) {
}
