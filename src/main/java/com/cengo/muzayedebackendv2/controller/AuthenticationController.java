package com.cengo.muzayedebackendv2.controller;

import com.cengo.muzayedebackendv2.domain.request.LoginRequest;
import com.cengo.muzayedebackendv2.domain.response.LoginResponse;
import com.cengo.muzayedebackendv2.security.permission.PermitAll;
import com.cengo.muzayedebackendv2.security.permission.RefreshPermit;
import com.cengo.muzayedebackendv2.security.permission.UserRole;
import com.cengo.muzayedebackendv2.service.authentication.AuthenticationService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;


    @PermitAll
    @PostMapping
    public ResponseEntity<LoginResponse> login(@RequestBody @Valid LoginRequest loginRequest, HttpServletResponse response) {
        return ResponseEntity.ok(authenticationService.authenticate(loginRequest, response));
    }

    @RefreshPermit
    @GetMapping("/refresh")
    public ResponseEntity<LoginResponse> refresh(HttpServletRequest request) {
        return ResponseEntity.ok(authenticationService.refresh(request));
    }

    @UserRole
    @GetMapping("/logout")
    public ResponseEntity<Object> logout(HttpServletResponse response) {
        authenticationService.logout(response);
        return ResponseEntity.ok().build();
    }
}
