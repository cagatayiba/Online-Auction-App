package com.cengo.muzayedebackendv2.repository;

import com.cengo.muzayedebackendv2.domain.entity.Bid;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface BidRepository extends JpaRepository<Bid, UUID> {
    List<Bid> findAllByLotIdOrderByCreateDate(UUID lotId);

    Bid findFirstByLotIdOrderByPriceDesc(UUID lotId);

    Bid findFirstByLotIdAndUserIdNotOrderByPriceDesc(UUID lotId, UUID userId);

    boolean existsByLotIdAndUserIdNotAndPriceLessThan(UUID lotId, UUID userId, Integer price);

    List<Bid> findAllByOfferId(UUID offerId);

    long countByLotId(UUID lotId);

    void deleteAllByLotIdAndUserId(UUID lotId, UUID userId);

}
