package com.cengo.muzayedebackendv2.controller;

import com.cengo.muzayedebackendv2.domain.response.lot.LotResponse;
import com.cengo.muzayedebackendv2.security.UserPrincipal;
import com.cengo.muzayedebackendv2.security.permission.PermitAll;
import com.cengo.muzayedebackendv2.service.LotService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/lots")
@RequiredArgsConstructor
public class LotController {

    private final LotService lotService;

    @PermitAll
    @GetMapping("/{lotId}")
    public ResponseEntity<LotResponse> getLot(@AuthenticationPrincipal UserPrincipal user,
                                                  @PathVariable UUID lotId) {
        return ResponseEntity.ok(lotService.getLotResponse(lotId, user));
    }
}
