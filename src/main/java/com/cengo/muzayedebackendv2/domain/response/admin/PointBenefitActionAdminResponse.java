package com.cengo.muzayedebackendv2.domain.response.admin;

import com.cengo.muzayedebackendv2.domain.enums.PointBenefitAction;
import jakarta.validation.constraints.NotNull;

public record PointBenefitActionAdminResponse(
        @NotNull PointBenefitAction action,
        @NotNull String text,
        @NotNull String description
) {
}
