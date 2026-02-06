package com.cengo.muzayedebackendv2.domain.response.admin;


import com.cengo.muzayedebackendv2.domain.response.user.UserBuyerResponse;

public record LotAdminSaleResponse(
        LotAdminResponse lot,
        UserBuyerResponse buyer,
        Boolean isTransferEnabled
) {
}
