package com.cengo.muzayedebackendv2.domain.entity.verification;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@ToString
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "verification_token")
public class VerificationToken {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @Column(name = "user_id")
    private UUID userId;
    @Column(name = "token")
    private UUID token = UUID.randomUUID();
    @Column(name = "expiry_date")
    private LocalDateTime expiryDate = LocalDateTime.now().plusMinutes(60);

    public VerificationToken(UUID userId) {
        this.userId = userId;
    }
}
