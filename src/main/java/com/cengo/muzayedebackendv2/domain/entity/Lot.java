package com.cengo.muzayedebackendv2.domain.entity;

import com.cengo.muzayedebackendv2.domain.entity.base.BaseEntity;
import com.cengo.muzayedebackendv2.domain.entity.enums.LotState;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Entity
@Table(name = "lot")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Lot extends BaseEntity {

    @NotBlank
    @Column(name = "name", nullable = false)
    private String name;

    @NotBlank
    @Column(name = "description", nullable = false)
    private String description;

    @Positive
    @NotNull
    @Column(name = "initial_price")
    private Integer initialPrice;

    @Column(name = "final_price")
    private Integer finalPrice;

    @NotNull
    @Column(name = "state", nullable = false)
    @Enumerated(EnumType.STRING)
    private LotState state = LotState.DRAFT;

    @NotNull
    @Column(name = "artist_id", nullable = false)
    private UUID artistId;

    @NotNull
    @Column(name = "auction_id", nullable = false)
    private UUID auctionId;

    @Column(name = "buyer_id")
    private UUID buyerId;

    @Column(name = "lot_number")
    private Integer lotNumber;

    @Column(name = "end_time")
    private LocalDateTime endTime;

    @Column(name = "extended_end_time")
    private LocalDateTime extendedEndTime;

    @Column(name = "winner_offer_id")
    private UUID winnerOfferId;

    public LocalDateTime getSaleEndTime() {
        return extendedEndTime == null ? endTime : extendedEndTime;
    }

    public Integer getCurrentPrice() {
        return finalPrice == null ? initialPrice : finalPrice;
    }

    public Optional<UUID> getWinnerOfferId() {
        return Optional.ofNullable(winnerOfferId);
    }

    public Boolean isEnd() {
        return getSaleEndTime().isBefore(LocalDateTime.now());
    }
}
