package com.cengo.muzayedebackendv2.service.verification;

import com.cengo.muzayedebackendv2.domain.dto.SendVerificationDTO;
import com.cengo.muzayedebackendv2.domain.entity.User;
import com.cengo.muzayedebackendv2.domain.entity.verification.VerificationToken;
import com.cengo.muzayedebackendv2.domain.response.VerificationResponse;
import com.cengo.muzayedebackendv2.repository.UserRepository;
import com.cengo.muzayedebackendv2.repository.VerificationRepository;
import com.cengo.muzayedebackendv2.service.mail.MailTemplateService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Validated
public class VerificationService {
    private final VerificationRepository verificationRepository;
    private final UserRepository userRepository;
    private final MailTemplateService mailTemplateService;
    private final Logger logger = LoggerFactory.getLogger(VerificationService.class);

    public void sendVerificationRequest(@Valid SendVerificationDTO request) {
        logger.info("Sending verification request");
        VerificationToken verificationToken = new VerificationToken(request.id());
        verificationRepository.save(verificationToken);
        mailTemplateService.sendVerificationMail(request, verificationToken.getToken());
    }

    public VerificationResponse verifyToken(UUID token) {
        VerificationToken verificationToken = verificationRepository.findByToken(token).orElse(null);

        if (verificationToken == null){
            String declineReason = "Geçersiz token.";
            return VerificationResponse.builder()
                    .verified(false)
                    .reason(declineReason)
                    .build();
        }

        verifyUser(verificationToken.getUserId());

        return VerificationResponse.builder()
                .verified(true)
                .userId(verificationToken.getUserId())
                .build();
    }

    public List<VerificationToken> getAllExpiredTokens(LocalDateTime now) {
        return verificationRepository.findAllByExpiryDateBefore(now);
    }

    public void deleteAllTokens(List<VerificationToken> expiredTokens) {
        verificationRepository.deleteAll(expiredTokens);
    }


    private void verifyUser(UUID userId) {
        User user = userRepository.findById(userId).orElse(null);
        if (user != null){
            user.setApproved(true);
            userRepository.save(user);
        }
    }
}
