package com.cengo.muzayedebackendv2.controller;

import com.cengo.muzayedebackendv2.domain.response.artist.ArtistListAuctionResponse;
import com.cengo.muzayedebackendv2.domain.response.auction.AuctionListResponse;
import com.cengo.muzayedebackendv2.domain.response.auction.AuctionResponse;
import com.cengo.muzayedebackendv2.security.UserPrincipal;
import com.cengo.muzayedebackendv2.security.permission.PermitAll;
import com.cengo.muzayedebackendv2.service.AuctionService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;


@RestController
@RequestMapping("/api/v1/auctions")
@RequiredArgsConstructor
public class AuctionController {

    private final AuctionService auctionService;

    @PermitAll
    @GetMapping("/current")
    public ResponseEntity<AuctionListResponse> getCurrentAuction() {
        return ResponseEntity.ok(auctionService.getCurrentAuctionResponse());
    }

    @PermitAll
    @GetMapping("/incoming")
    public ResponseEntity<Page<AuctionListResponse>> getIncomingAuctions(Pageable pageable) {
        return ResponseEntity.ok(auctionService.getIncomingAuctions(pageable));
    }

    @PermitAll
    @GetMapping("/pasts")
    public ResponseEntity<Page<AuctionListResponse>> getPastAuctions(Pageable pageable) {
        return ResponseEntity.ok(auctionService.getPastAuctions(pageable));
    }

    @PermitAll
    @GetMapping("/{auctionId}/lots")
    public ResponseEntity<AuctionResponse> getAuctionLots(@AuthenticationPrincipal UserPrincipal user,
                                                          @PathVariable UUID auctionId,
                                                          Pageable pageable) {
        return ResponseEntity.ok(auctionService.getAuctionLots(auctionId, user, pageable));
    }

    @PermitAll
    @GetMapping("/{auctionId}/lots/new")
    public ResponseEntity<AuctionResponse> getAuctionLotsV2(@AuthenticationPrincipal UserPrincipal user,
                                                          @PathVariable UUID auctionId,
                                                          @RequestParam(required = false) String filter,
                                                          Pageable pageable) {
        return ResponseEntity.ok(auctionService.getAuctionLotsV2(auctionId, user,filter, pageable));
    }


    @PermitAll
    @GetMapping("/{auctionId}/artists")
    public ResponseEntity<List<ArtistListAuctionResponse>> getAuctionArtists(@PathVariable UUID auctionId){
        return ResponseEntity.ok(auctionService.getArtistsOfAuction(auctionId));
    }
}
