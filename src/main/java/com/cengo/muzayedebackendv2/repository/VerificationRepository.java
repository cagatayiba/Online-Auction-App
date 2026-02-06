package com.cengo.muzayedebackendv2.repository;

import com.cengo.muzayedebackendv2.domain.entity.verification.VerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface VerificationRepository extends JpaRepository<VerificationToken, UUID> {
    Optional<VerificationToken> findByToken(UUID token);
    List<VerificationToken> findAllByExpiryDateBefore(LocalDateTime currentDate);
}
