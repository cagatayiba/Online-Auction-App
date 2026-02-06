package com.cengo.muzayedebackendv2.repository;

import com.cengo.muzayedebackendv2.domain.entity.video.ArtistBiographyVideo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ArtistBiographyVideoRepository extends JpaRepository<ArtistBiographyVideo, UUID> {
    List<ArtistBiographyVideo> findAllByArtistIdOrderByOrder(UUID artistId);
}
