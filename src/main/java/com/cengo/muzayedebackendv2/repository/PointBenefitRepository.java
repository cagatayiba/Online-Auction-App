package com.cengo.muzayedebackendv2.repository;

import com.cengo.muzayedebackendv2.domain.entity.PointBenefit;
import com.cengo.muzayedebackendv2.domain.enums.PointBenefitAction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PointBenefitRepository extends JpaRepository<PointBenefit, UUID> {

    Optional<PointBenefit> findByActionAndIsActiveTrue(PointBenefitAction pointBenefitAction);

    List<PointBenefit> findAllByIsActiveTrue();
}
