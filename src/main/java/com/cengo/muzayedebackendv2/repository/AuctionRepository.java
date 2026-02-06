package com.cengo.muzayedebackendv2.repository;

import com.cengo.muzayedebackendv2.domain.entity.Auction;
import com.cengo.muzayedebackendv2.domain.entity.enums.AuctionState;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AuctionRepository extends JpaRepository<Auction, UUID> {
    boolean existsByEndTimeAfterAndStateNotAndIdNot(LocalDateTime startTime, AuctionState state, UUID id);

    Optional<Auction> findByState(AuctionState auctionState);

    List<Auction> findAllByStateInOrderByStartTime(List<AuctionState> auctionStates);

    Page<Auction> findAllByStateOrderByEndTimeDesc(AuctionState auctionState, Pageable pageable);

    List<Auction> findAllByStateOrderByName(AuctionState auctionState);

    Optional<Auction> findByStateAndStartTimeIsBefore(AuctionState auctionState, LocalDateTime now);
}
