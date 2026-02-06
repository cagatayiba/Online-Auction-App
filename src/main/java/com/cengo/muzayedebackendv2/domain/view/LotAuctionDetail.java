package com.cengo.muzayedebackendv2.domain.view;


import com.amazonaws.annotation.Immutable;
import com.cengo.muzayedebackendv2.domain.entity.enums.AuctionState;
import com.cengo.muzayedebackendv2.domain.entity.enums.LotState;
import com.cengo.muzayedebackendv2.domain.view.key.LotAuctionDetailKey;
import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "lot_auction_detail_view")
@Immutable
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class LotAuctionDetail {

    @EmbeddedId
    private LotAuctionDetailKey id;

    // from lot
    @Column(name = "auction_id")
    private UUID auctionId;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "current_price")
    private Integer currentPrice;

    @Column(name = "state")
    @Enumerated(EnumType.STRING)
    private LotState state;

    @Column(name = "lot_number")
    private Integer lotNumber;

    @Column(name = "artist_id")
    private UUID artistId;

    // generated
    @Column(name = "sale_end_time")
    private LocalDateTime saleEndTime;

    @Column(name = "is_bid_placed")
    private Boolean isBidPlaced;

    @Column(name = "is_in_watchlist")
    private Boolean isInWatchlist;

    @Column(name = "is_winner")
    private Boolean isWinner;

    @Column(name = "bid_count")
    private Integer bidCount;

    // from auction
    @Column(name = "auction_state")
    @Enumerated(EnumType.STRING)
    private AuctionState auctionState;

    @Column(name = "auction_start_time")
    private LocalDateTime auctionStartTime;
}
