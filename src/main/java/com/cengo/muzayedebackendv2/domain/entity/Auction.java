package com.cengo.muzayedebackendv2.domain.entity;

import com.cengo.muzayedebackendv2.domain.entity.base.BaseEntity;
import com.cengo.muzayedebackendv2.domain.entity.enums.AuctionState;
import com.cengo.muzayedebackendv2.validation.annotation.NonConflictAuction;
import com.cengo.muzayedebackendv2.validation.annotation.ValidTimes;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "auction")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ValidTimes
@NonConflictAuction
public class Auction extends BaseEntity {

    @NotBlank
    @Column(name = "name", nullable = false)
    private String name;

    @NotBlank
    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "auction_no")
    private Integer auctionNo;

    @NotNull
    @Column(name = "start_time", nullable = false)
    private LocalDateTime startTime;

    @NotNull
    @Column(name = "end_time", nullable = false)
    private LocalDateTime endTime;

    @NotNull
    @Column(name = "state", nullable = false)
    @Enumerated(EnumType.STRING)
    private AuctionState state = AuctionState.DRAFT;

    public Boolean isCurrentAuction() {
        return state.isStarted();
    }
}
