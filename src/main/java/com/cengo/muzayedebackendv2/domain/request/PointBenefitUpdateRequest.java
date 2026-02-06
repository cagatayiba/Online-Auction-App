package com.cengo.muzayedebackendv2.domain.request;

import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public record PointBenefitUpdateRequest(
        @NotNull UUID id,
        @NotNull Integer amount,
        @NotNull Boolean isActive
) {}
