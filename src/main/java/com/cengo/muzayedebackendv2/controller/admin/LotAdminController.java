package com.cengo.muzayedebackendv2.controller.admin;

import com.cengo.muzayedebackendv2.domain.request.LotSaveRequest;
import com.cengo.muzayedebackendv2.domain.request.LotUpdateRequest;
import com.cengo.muzayedebackendv2.domain.response.admin.LotAdminResponse;
import com.cengo.muzayedebackendv2.domain.response.admin.LotAdminSaleResponse;
import com.cengo.muzayedebackendv2.domain.response.admin.LotAdminTransferResponse;
import com.cengo.muzayedebackendv2.security.permission.AdminRole;
import com.cengo.muzayedebackendv2.service.LotService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@AdminRole
@RestController
@RequestMapping("/api/v1/admin/lots")
@RequiredArgsConstructor
public class LotAdminController {

    private final LotService lotService;

    @PostMapping
    public ResponseEntity<LotAdminResponse> save(@RequestPart @Valid LotSaveRequest lotSaveRequest,
                                            @RequestPart(required = false) MultipartFile coverImage,
                                            @RequestPart(required = false) List<MultipartFile> assets) {
        return ResponseEntity.ok(lotService.saveLot(lotSaveRequest, coverImage, assets));
    }

    @PostMapping("/{lotId}/confirm")
    public ResponseEntity<LotAdminSaleResponse> confirmSale(@PathVariable UUID lotId) {
        return ResponseEntity.ok(lotService.confirmSale(lotId));
    }

    @PostMapping("/{lotId}/transfer")
    public ResponseEntity<LotAdminSaleResponse> transfer(@PathVariable UUID lotId) {
        return ResponseEntity.ok(lotService.transferOwner(lotId));
    }

    @PostMapping("/{lotId}/cancel")
    public ResponseEntity<LotAdminSaleResponse> cancelSale(@PathVariable UUID lotId) {
        return ResponseEntity.ok(lotService.cancelSale(lotId));
    }

    @PutMapping("/{lotId}")
    public ResponseEntity<LotAdminResponse> update(@PathVariable UUID lotId,
                                              @RequestBody @Valid LotUpdateRequest lotUpdateRequest) {
        return ResponseEntity.ok(lotService.updateLot(lotId, lotUpdateRequest));
    }

    @DeleteMapping("/{lotId}")
    public ResponseEntity<Object> delete(@PathVariable UUID lotId) {
        lotService.deleteLotById(lotId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{lotId}")
    public ResponseEntity<LotAdminResponse> getLot(@PathVariable UUID lotId) {
        return ResponseEntity.ok(lotService.adminGetLotById(lotId));
    }

    @GetMapping("/{lotId}/transfer")
    public ResponseEntity<LotAdminTransferResponse> getTransferInfo(@PathVariable UUID lotId) {
        return ResponseEntity.ok(lotService.getTransferInfo(lotId));
    }
}
