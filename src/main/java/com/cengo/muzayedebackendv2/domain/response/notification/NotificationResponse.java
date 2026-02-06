package com.cengo.muzayedebackendv2.domain.response.notification;

import com.cengo.muzayedebackendv2.domain.enums.NotificationType;

import java.util.UUID;

public record NotificationResponse(
        UUID id,
        NotificationType type,
        Boolean isRead,
        String message,
        String link
) {
}
