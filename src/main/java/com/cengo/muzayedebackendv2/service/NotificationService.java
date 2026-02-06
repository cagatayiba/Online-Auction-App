package com.cengo.muzayedebackendv2.service;

import com.cengo.muzayedebackendv2.domain.dto.SendNotificationDTO;
import com.cengo.muzayedebackendv2.domain.entity.Notification;
import com.cengo.muzayedebackendv2.domain.response.notification.NotificationListResponse;
import com.cengo.muzayedebackendv2.mapper.NotificationMapper;
import com.cengo.muzayedebackendv2.repository.NotificationRepository;
import com.cengo.muzayedebackendv2.service.base.BaseEntityService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.UUID;

@Service
@Validated
public class NotificationService extends BaseEntityService<Notification, NotificationRepository>  {

    private final NotificationMapper notificationMapper;

    protected NotificationService(NotificationRepository repository, NotificationMapper notificationMapper) {
        super(repository);
        this.notificationMapper = notificationMapper;
    }


    public void saveNotification(@Valid SendNotificationDTO request) {
        save(notificationMapper.convertToNotification(request));
    }

    public NotificationListResponse getNotifications(UUID userId, Pageable pageable) {
        var notifications = getRepository().findAllByUserId(userId, pageable);
        var notificationList = notifications.map(notificationMapper::convertToNotificationResponse);
        return notificationMapper.convertToNotificationListResponse(notificationList);
    }

    public void markAsRead(List<UUID> notificationIds) {
        var notifications = getRepository().findAllByIdIn(notificationIds);
        notifications.forEach(Notification::setRead);
        saveAll(notifications);
    }

    public void deleteNotification(UUID notificationId) {
        var notification = getById(notificationId);
        delete(notification);
    }
}
