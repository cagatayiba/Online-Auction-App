package com.cengo.muzayedebackendv2.repository.spec;

import com.cengo.muzayedebackendv2.domain.view.LotAuctionDetail;
import org.springframework.data.jpa.domain.Specification;

import java.util.UUID;

public class LotAuctionDetailSpecs {

    public static Specification<LotAuctionDetail> auctionIdEquals(UUID auctionId) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("auctionId"), auctionId);
    }

    public static Specification<LotAuctionDetail> userIdEquals(UUID userId) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("id").get("userId"), userId);
    }
}
