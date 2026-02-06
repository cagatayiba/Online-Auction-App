package com.cengo.muzayedebackendv2.domain.entity.enums;

import lombok.Getter;

@Getter
public enum UserRole {
    USER,
    ADMIN,
    OBSERVER;

    @Override
    public String toString() {
        return "ROLE_" + this.name();
    }
}
