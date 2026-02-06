package com.cengo.muzayedebackendv2.mapper;

import com.cengo.muzayedebackendv2.domain.dto.SendNotificationDTO;
import com.cengo.muzayedebackendv2.domain.entity.Notification;
import com.cengo.muzayedebackendv2.domain.response.notification.NotificationListResponse;
import com.cengo.muzayedebackendv2.domain.response.notification.NotificationResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;
import org.springframework.data.domain.Page;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface NotificationMapper {

    @Mapping(target = "message", source = "request", qualifiedByName = "mapMessage")
    Notification convertToNotification(SendNotificationDTO request);

    NotificationResponse convertToNotificationResponse(Notification notification);

    @Mapping(target = "unreadCount", source = "notificationList", qualifiedByName = "mapCount")
    @Mapping(target = "notificationList", source = "notificationList")
    NotificationListResponse convertToNotificationListResponse(Page<NotificationResponse> notificationList);

    @Named("mapMessage")
    default String mapMessage(SendNotificationDTO request) {
        var type = request.type();
        return type.getMessage(request.productName());
    }

    @Named("mapCount")
    default Long mapCount(Page<NotificationResponse> notificationList) {
        return notificationList
                .stream()
                .filter(notification -> !notification.isRead())
                .count();
    }
}
