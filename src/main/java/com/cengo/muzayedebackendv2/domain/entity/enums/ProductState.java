package com.cengo.muzayedebackendv2.domain.entity.enums;

import lombok.Getter;

@Getter
public enum ProductState {
    ON_SALE("SATIŞTA"),
    SALE_REQUEST("SATILDI"),
    SOLD("SATILDI");

    private final String bannerText;

    ProductState(String bannerText) {
        this.bannerText = bannerText;
    }

    public Boolean isUpdatable() {
        return this == ON_SALE;
    }

    public Boolean isRequested() {
        return this == SALE_REQUEST;
    }
}
