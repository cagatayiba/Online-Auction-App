package com.cengo.muzayedebackendv2.controller.admin;

import com.cengo.muzayedebackendv2.domain.request.AuctionSaveRequest;
import com.cengo.muzayedebackendv2.domain.request.AuctionUpdateRequest;
import com.cengo.muzayedebackendv2.domain.response.admin.*;
import com.cengo.muzayedebackendv2.security.permission.AdminRole;
import com.cengo.muzayedebackendv2.service.AuctionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@AdminRole
@RestController
@RequestMapping("/api/v1/admin/auctions")
@RequiredArgsConstructor
public class AuctionAdminController {

    private final AuctionService auctionService;

    @PostMapping
    public ResponseEntity<AuctionAdminResponse> save(@RequestPart @Valid AuctionSaveRequest auctionSaveRequest,
                                                     @RequestPart(required = false) MultipartFile image) {
        return ResponseEntity.ok(auctionService.saveAuction(auctionSaveRequest, image));
    }

    @PutMapping("/{auctionId}")
    public ResponseEntity<AuctionAdminResponse> update(@PathVariable UUID auctionId,
                                                       @RequestBody @Valid AuctionUpdateRequest auctionUpdateRequest) {
        return ResponseEntity.ok(auctionService.updateAuction(auctionId, auctionUpdateRequest));
    }

    @DeleteMapping("/{auctionId}")
    public ResponseEntity<Object> delete(@PathVariable UUID auctionId) {
        auctionService.deleteAuction(auctionId);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{auctionId}/set-ready")
    public ResponseEntity<AuctionAdminResponse> setReady(@PathVariable UUID auctionId) {
        return ResponseEntity.ok(auctionService.setReady(auctionId));
    }

    @PatchMapping("/{auctionId}/set-draft")
    public ResponseEntity<AuctionAdminResponse> setDraft(@PathVariable UUID auctionId) {
        return ResponseEntity.ok(auctionService.setDraft(auctionId));
    }

    @GetMapping("/ready")
    public ResponseEntity<List<AuctionAdminListResponse>> getReadyAuctions() {
        return ResponseEntity.ok(auctionService.adminGetReadyAuctions());
    }

    @GetMapping("/drafts")
    public ResponseEntity<List<AuctionAdminListResponse>> getDraftAuctions() {
        return ResponseEntity.ok(auctionService.adminGetDraftAuctions());
    }

    @GetMapping("/{auctionId}")
    public ResponseEntity<AuctionAdminResponse> getAuction(@PathVariable UUID auctionId) {
        return ResponseEntity.ok(auctionService.adminGetAuctionById(auctionId));
    }

    @GetMapping("/{auctionId}/lots")
    public ResponseEntity<Page<LotAdminResponse>> getAuctionLots(@PathVariable UUID auctionId, Pageable pageable) {
        return ResponseEntity.ok(auctionService.adminGetAuctionLots(auctionId, pageable));
    }

    @GetMapping("/{auctionId}/sales")
    public ResponseEntity<Page<LotAdminSaleResponse>> getAuctionSales(@PathVariable UUID auctionId, Pageable pageable) {
        return ResponseEntity.ok(auctionService.adminGetAuctionSales(auctionId, pageable));
    }
}
