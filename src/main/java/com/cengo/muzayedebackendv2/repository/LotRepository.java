package com.cengo.muzayedebackendv2.repository;

import com.cengo.muzayedebackendv2.domain.entity.Lot;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface LotRepository extends JpaRepository<Lot, UUID> {
    List<Lot> findAllByAuctionId(UUID auctionId);

    List<Lot> findAllByArtistId(UUID artistId);

    Page<Lot> findAllByAuctionIdOrderByLotNumber(UUID auctionId, Pageable pageable);

    List<Lot> findAllByBuyerId(UUID buyerId);

    List<Lot> findAllByBuyerIdAndAuctionId(UUID userId, UUID auctionId);
}
