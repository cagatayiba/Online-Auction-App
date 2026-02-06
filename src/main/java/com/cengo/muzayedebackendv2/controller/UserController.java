package com.cengo.muzayedebackendv2.controller;

import com.cengo.muzayedebackendv2.domain.response.auction.AuctionWinningResponse;
import com.cengo.muzayedebackendv2.domain.response.lot.LotWatchlistResponse;
import com.cengo.muzayedebackendv2.domain.request.UserSaveRequest;
import com.cengo.muzayedebackendv2.domain.request.UserUpdatePasswordRequest;
import com.cengo.muzayedebackendv2.domain.request.UserUpdateRequest;
import com.cengo.muzayedebackendv2.domain.response.lot.LotWinningResponse;
import com.cengo.muzayedebackendv2.domain.response.product.ProductOrderResponse;
import com.cengo.muzayedebackendv2.domain.response.user.UserResponse;
import com.cengo.muzayedebackendv2.security.UserPrincipal;
import com.cengo.muzayedebackendv2.security.permission.PermitAll;
import com.cengo.muzayedebackendv2.security.permission.UserRole;
import com.cengo.muzayedebackendv2.service.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;


@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final WatchlistService watchlistService;
    private final AuctionService auctionService;
    private final LotService lotService;
    private final ProductService productService;

    @PermitAll
    @PostMapping
    public ResponseEntity<UserResponse> save(@RequestBody @Valid UserSaveRequest userSaveRequest) {
        return ResponseEntity.ok(userService.saveUser(userSaveRequest));
    }

    @UserRole
    @GetMapping("/me")
    public ResponseEntity<UserResponse> getMe(@AuthenticationPrincipal UserPrincipal user) {
        return ResponseEntity.ok(userService.getUserResponseById(user.getUserId()));
    }

    @UserRole
    @PatchMapping
    public ResponseEntity<UserResponse> update(@AuthenticationPrincipal UserPrincipal user,
                                               @RequestBody @Valid UserUpdateRequest userUpdateRequest) {
        return ResponseEntity.ok(userService.updateUser(user.getUserId(), userUpdateRequest));
    }

    @UserRole
    @PatchMapping("/password")
    public ResponseEntity<UserResponse> updatePassword(@AuthenticationPrincipal UserPrincipal user,
                                                       @RequestBody @Valid UserUpdatePasswordRequest userUpdatePasswordRequest) {
        return ResponseEntity.ok(userService.updateUserPassword(user.getUserId(), userUpdatePasswordRequest));
    }

    @UserRole
    @GetMapping("/watchlist")
    public ResponseEntity<Page<LotWatchlistResponse>> getWatchlist(@AuthenticationPrincipal UserPrincipal user, Pageable pageable){
        return ResponseEntity.ok(watchlistService.getUserWatchlist(user.getUserId(), pageable));
    }

    @UserRole
    @GetMapping("/winnings")
    public ResponseEntity<List<AuctionWinningResponse>> getWinnings(@AuthenticationPrincipal UserPrincipal user){
        return ResponseEntity.ok(auctionService.getUserWinnings(user.getUserId()));
    }

    @UserRole
    @GetMapping("/orders")
    public ResponseEntity<List<ProductOrderResponse>> getOrders(@AuthenticationPrincipal UserPrincipal user){
        return ResponseEntity.ok(productService.getUserOrders(user.getUserId()));
    }

    @UserRole
    @GetMapping("/winnings/{auctionId}")
    public ResponseEntity<List<LotWinningResponse>> getWinningProducts(@AuthenticationPrincipal UserPrincipal user, @PathVariable UUID auctionId) {
        return ResponseEntity.ok(lotService.getUserWinningLots(user.getUserId(), auctionId));
    }
}
