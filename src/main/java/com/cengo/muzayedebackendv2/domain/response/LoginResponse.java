package com.cengo.muzayedebackendv2.domain.response;

import com.cengo.muzayedebackendv2.domain.response.user.UserLoginResponse;
import lombok.Builder;

@Builder
public record LoginResponse (
        String token,
        UserLoginResponse user,
        Boolean admin
) {
}
