package com.cengo.muzayedebackendv2.domain.entity.enums;

public enum LotState {
    DRAFT,
    READY,
    ON_SALE,
    SOLD,
    NOT_SOLD,
    // TODO bence satış iptallerinde bunu koyabiliriz
    SALE_CANCELLED,
    APPROVED;


    public Boolean isUpdatable() {
        return this == DRAFT;
    }

    public Boolean isStarted() {
        return this == ON_SALE;
    }

    public Boolean isEnded() {
        return this == SOLD || this == NOT_SOLD;
    }

    public String getBannerText(boolean isWinner, boolean isCurrentAuction) {
        if (isCurrentAuction) {
            if (this == SOLD || this == NOT_SOLD) {
                return "KAPANDI";
            }
        }else {
            if (isWinner) {
                return "SATIN ALDINIZ";
            }

            if (this == SOLD) {
                return "SATILDI";
            } else if (this == NOT_SOLD) {
                return "SATILMADI";
            }
        }
        return null;
    }
}
