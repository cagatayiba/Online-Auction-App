package com.cengo.muzayedebackendv2.domain.response.admin;

import com.cengo.muzayedebackendv2.domain.enums.PointBenefitAction;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public record PointBenefitAdminResponse(
        @NotNull UUID id,
        @NotNull PointBenefitAction action,
        @NotNull Integer amount,
        @NotNull Boolean isActive
) { }
