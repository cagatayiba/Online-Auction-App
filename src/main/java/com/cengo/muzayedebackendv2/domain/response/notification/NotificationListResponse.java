package com.cengo.muzayedebackendv2.domain.response.notification;

import org.springframework.data.domain.Page;

public record NotificationListResponse(
        Page<NotificationResponse> notificationList,
        Long unreadCount
) {
}
