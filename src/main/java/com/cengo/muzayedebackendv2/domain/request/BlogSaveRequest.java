package com.cengo.muzayedebackendv2.domain.request;

import jakarta.validation.constraints.NotBlank;

public record BlogSaveRequest(
        @NotBlank String content,
        @NotBlank String name,
        @NotBlank String keywords,
        @NotBlank String metaDescription,
        @NotBlank String pageTitle
) {
}
