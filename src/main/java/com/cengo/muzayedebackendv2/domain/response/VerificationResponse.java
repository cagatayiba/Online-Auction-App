package com.cengo.muzayedebackendv2.domain.response;


import lombok.Builder;

import java.util.UUID;

@Builder
public record VerificationResponse(
        Boolean verified,
        UUID userId,
        String reason
) {
}
