package com.cengo.muzayedebackendv2.domain.entity;

import com.cengo.muzayedebackendv2.domain.entity.base.BaseEntity;
import com.cengo.muzayedebackendv2.domain.enums.NotificationType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "notification")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Notification extends BaseEntity {

    @NotNull
    @Column(name = "user_id", nullable = false)
    private UUID userId;

    @NotNull
    @Column(name = "type", nullable = false)
    @Enumerated(EnumType.STRING)
    private NotificationType type;

    @NotNull
    @Column(name = "is_read", nullable = false)
    private Boolean isRead = false;

    @NotBlank
    @Column(name = "message", nullable = false)
    private String message;

    @NotBlank
    @Column(name = "link", nullable = false)
    private String link;

    public void setRead() {
        this.isRead = true;
    }
}
