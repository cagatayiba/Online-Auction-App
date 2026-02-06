package com.cengo.muzayedebackendv2.repository;

import com.cengo.muzayedebackendv2.domain.entity.Asset;
import com.cengo.muzayedebackendv2.domain.entity.enums.AssetDomainType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface AssetRepository extends JpaRepository<Asset, UUID> {
    Asset findByReferenceId(UUID referenceId);

    Asset findByReferenceIdAndDomainType(UUID referenceId, AssetDomainType type);

    List<Asset> findAllByReferenceIdAndDomainTypeOrderByOrderNo(UUID referenceId, AssetDomainType type);

    List<Asset> findAllByReferenceId(UUID referenceId);
}
