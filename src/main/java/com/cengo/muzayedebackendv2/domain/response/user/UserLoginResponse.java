package com.cengo.muzayedebackendv2.domain.response.user;


import java.util.UUID;

public record UserLoginResponse(
        UUID id,
        String name,
        String surname
) {
}
