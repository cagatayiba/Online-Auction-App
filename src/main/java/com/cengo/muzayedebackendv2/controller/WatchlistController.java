package com.cengo.muzayedebackendv2.controller;

import com.cengo.muzayedebackendv2.security.UserPrincipal;
import com.cengo.muzayedebackendv2.security.permission.UserRole;
import com.cengo.muzayedebackendv2.service.WatchlistService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@UserRole
@RestController
@RequestMapping("/api/v1/watchlist")
@RequiredArgsConstructor
public class WatchlistController {

    private final WatchlistService watchlistService;


    @PostMapping("/{lotId}")
    public ResponseEntity<Object> saveWatchList(@AuthenticationPrincipal UserPrincipal user, @PathVariable UUID lotId) {
        watchlistService.saveWatchlist(user.getUserId(), lotId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{lotId}")
    public ResponseEntity<Object> deleteWatchList(@AuthenticationPrincipal UserPrincipal user, @PathVariable UUID lotId) {
        watchlistService.deleteWatchList(user.getUserId(), lotId);
        return ResponseEntity.ok().build();
    }
}
