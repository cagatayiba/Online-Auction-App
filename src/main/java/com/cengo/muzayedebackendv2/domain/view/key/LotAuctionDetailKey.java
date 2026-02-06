package com.cengo.muzayedebackendv2.domain.view.key;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.io.Serializable;
import java.util.UUID;


@Embeddable
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class LotAuctionDetailKey implements Serializable {
    @Column(name = "lot_id")
    private UUID lotId;

    @Column(name = "user_id")
    private UUID userId;
}
