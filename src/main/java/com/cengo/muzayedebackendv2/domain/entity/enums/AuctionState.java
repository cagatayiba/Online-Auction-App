package com.cengo.muzayedebackendv2.domain.entity.enums;

public enum AuctionState {
    DRAFT,
    READY,
    STARTED,
    COMPLETED;

    public Boolean isUpdatable() {
        return this == DRAFT;
    }

    public Boolean isDraftable() {
        return this == READY || this == DRAFT;
    }

    public Boolean isStarted() {
        return this == STARTED;
    }
}
