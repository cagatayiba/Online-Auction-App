package com.cengo.muzayedebackendv2.domain.entity;

import com.cengo.muzayedebackendv2.domain.entity.base.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "offer")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Offer extends BaseEntity {

    @Positive
    @NotNull
    @Column(name = "price", nullable = false)
    private Integer price;

    @NotNull
    @Column(name = "time", nullable = false)
    private LocalDateTime time = LocalDateTime.now();

    @NotNull
    @Column(name = "user_id", nullable = false)
    private UUID userId;

    @NotNull
    @Column(name = "lot_id", nullable = false)
    private UUID lotId;

    public Boolean isOfferFromSameUser(UUID userId) {
        return this.userId.equals(userId);
    }
}
