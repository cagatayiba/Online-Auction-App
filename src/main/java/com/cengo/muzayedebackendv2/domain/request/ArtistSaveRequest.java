package com.cengo.muzayedebackendv2.domain.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record ArtistSaveRequest(
        @NotBlank String fullName,
        @NotBlank String summary,
        @NotBlank String biography,
        @NotNull List<ArtistDescriptionVideoRequest> descriptionVideos
) {
}
