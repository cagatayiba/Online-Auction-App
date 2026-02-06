package com.cengo.muzayedebackendv2.domain.response.point;

import jakarta.validation.constraints.NotBlank;
import java.util.List;

public record PointEarningOptionsResponse(
        @NotBlank List<String> optionDescriptions
) { }
