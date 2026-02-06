package com.cengo.muzayedebackendv2.domain.response.user;

import com.cengo.muzayedebackendv2.domain.entity.enums.UserRole;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;

public record UserResponse (
        String name,
        String surname,
        String email,
        String phone,
        String address,
        @JsonFormat(pattern = "dd-MM-yyyy")
        LocalDate birthDate,
        UserRole role,
        Integer point
) {
}
