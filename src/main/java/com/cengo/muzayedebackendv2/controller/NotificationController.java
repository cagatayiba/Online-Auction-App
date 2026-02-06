package com.cengo.muzayedebackendv2.controller;

import com.cengo.muzayedebackendv2.domain.response.notification.NotificationListResponse;
import com.cengo.muzayedebackendv2.security.UserPrincipal;
import com.cengo.muzayedebackendv2.security.permission.UserRole;
import com.cengo.muzayedebackendv2.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@UserRole
@RestController
@RequestMapping("/api/v1/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping
    public ResponseEntity<NotificationListResponse> getNotifications(@AuthenticationPrincipal UserPrincipal user, Pageable pageable) {
        return ResponseEntity.ok(notificationService.getNotifications(user.getUserId(), pageable));
    }

    @PutMapping("/{notificationIds}")
    public ResponseEntity<Object> markAsRead(@PathVariable List<UUID> notificationIds) {
        notificationService.markAsRead(notificationIds);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{notificationId}")
    public ResponseEntity<Object> deleteNotification(@PathVariable UUID notificationId) {
        notificationService.deleteNotification(notificationId);
        return ResponseEntity.ok().build();
    }
}
