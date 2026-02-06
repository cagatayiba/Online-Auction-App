package com.cengo.muzayedebackendv2.repository;

import com.cengo.muzayedebackendv2.domain.entity.WatchList;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface WatchlistRepository extends JpaRepository<WatchList, UUID> {
    Optional<WatchList> findByUserIdAndLotId(UUID userId, UUID lotId);

    void deleteByUserIdAndLotId(UUID userId, UUID lotId);

    Page<WatchList> findAllByUserId(UUID userId, Pageable pageable);

    void deleteByCreateDateBefore(LocalDateTime createDate);
}
