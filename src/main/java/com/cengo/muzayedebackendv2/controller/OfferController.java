package com.cengo.muzayedebackendv2.controller;

import com.cengo.muzayedebackendv2.domain.request.OfferRequest;
import com.cengo.muzayedebackendv2.domain.response.SaveOfferResponse;
import com.cengo.muzayedebackendv2.domain.response.PriceOptionsResponse;
import com.cengo.muzayedebackendv2.security.UserPrincipal;
import com.cengo.muzayedebackendv2.security.permission.UserRole;
import com.cengo.muzayedebackendv2.service.OfferService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@UserRole
@RestController
@RequestMapping("/api/v1/bids")
@RequiredArgsConstructor
public class OfferController {
    private final OfferService offerService;

    @PostMapping
    public ResponseEntity<SaveOfferResponse> save(@AuthenticationPrincipal UserPrincipal user,
                                                  @RequestBody @Valid OfferRequest offerRequest) {
        return ResponseEntity.ok(offerService.saveOffer(offerRequest, user.getUserId()));
    }

    @GetMapping("/price-options/{lotId}")
    public ResponseEntity<PriceOptionsResponse> getPriceOptions(
            @AuthenticationPrincipal UserPrincipal user,
            @PathVariable UUID lotId
    ) {
        return ResponseEntity.ok(offerService.getPriceOptions(lotId, user.getUserId()));
    }
}
