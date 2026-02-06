package com.cengo.muzayedebackendv2.domain.response.user;

public record UserBuyerResponse(
        String name,
        String surname,
        String email,
        String phone,
        String address
) {
}
