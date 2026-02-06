package com.cengo.muzayedebackendv2.repository;

import com.cengo.muzayedebackendv2.domain.dto.ArtistNameDTO;
import com.cengo.muzayedebackendv2.domain.entity.Artist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ArtistRepository extends JpaRepository<Artist, UUID>, JpaSpecificationExecutor<Artist> {
    List<Artist> findAllByOrderByFullName();

    @Query(value = "SELECT a.id AS id, a.full_name AS fullName FROM artist a WHERE a.id IN :ids ORDER BY fullName", nativeQuery = true)
    List<ArtistNameDTO> findNameInfoByIdsOrderByName(Iterable<UUID> ids);
}
