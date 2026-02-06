package com.cengo.muzayedebackendv2.controller.admin;

import com.cengo.muzayedebackendv2.domain.response.AssetResponse;
import com.cengo.muzayedebackendv2.security.permission.AdminRole;
import com.cengo.muzayedebackendv2.service.AssetService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@AdminRole
@RestController
@RequestMapping("/api/v1/admin/assets")
@RequiredArgsConstructor
public class AssetController {

    private final AssetService assetService;

    @PostMapping("/{referenceId}")
    public ResponseEntity<AssetResponse> save(@PathVariable UUID referenceId,
                                              @RequestPart MultipartFile file) {
        return ResponseEntity.ok(assetService.saveAssetRequest(referenceId, file));
    }

    @PutMapping("/{assetId}")
    public ResponseEntity<AssetResponse> update(@PathVariable UUID assetId,
                                                @RequestPart MultipartFile file) {
        return ResponseEntity.ok(assetService.updateAsset(assetId, file));
    }

    @DeleteMapping("/{assetId}")
    public ResponseEntity<Object> delete(@PathVariable UUID assetId) {
        assetService.deleteAsset(assetId);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{id1}/{id2}")
    public ResponseEntity<List<AssetResponse>> changeOrder(@PathVariable UUID id1,
                                                           @PathVariable UUID id2) {
        return ResponseEntity.ok(assetService.changeOrder(List.of(id1, id2)));
    }
}
