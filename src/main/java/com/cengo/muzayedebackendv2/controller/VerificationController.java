package com.cengo.muzayedebackendv2.controller;

import com.cengo.muzayedebackendv2.domain.response.VerificationResponse;
import com.cengo.muzayedebackendv2.security.permission.PermitAll;
import com.cengo.muzayedebackendv2.service.UserService;
import com.cengo.muzayedebackendv2.service.verification.VerificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@PermitAll
@RestController
@RequestMapping("/api/v1/verification")
@RequiredArgsConstructor
public class VerificationController {

    private final VerificationService verificationService;
    private final UserService userService;


    @GetMapping("/{token}")
    public ResponseEntity<VerificationResponse> verify(@PathVariable UUID token) {
        return ResponseEntity.ok(verificationService.verifyToken(token));
    }

    @PostMapping("/resend/{userId}")
    public ResponseEntity<Object> resend(@PathVariable UUID userId) {
        userService.resendVerification(userId);
        return ResponseEntity.ok().build();
    }

}
