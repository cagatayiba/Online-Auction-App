package com.cengo.muzayedebackendv2.controller.admin;

import com.cengo.muzayedebackendv2.domain.request.ArtistSaveRequest;
import com.cengo.muzayedebackendv2.domain.request.ArtistUpdateRequest;
import com.cengo.muzayedebackendv2.domain.response.admin.ArtistAdminListResponse;
import com.cengo.muzayedebackendv2.domain.response.admin.ArtistAdminNameResponse;
import com.cengo.muzayedebackendv2.domain.response.admin.ArtistAdminResponse;
import com.cengo.muzayedebackendv2.security.permission.AdminRole;
import com.cengo.muzayedebackendv2.service.ArtistService;
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
@RequestMapping("/api/v1/admin/artists")
@RequiredArgsConstructor
public class ArtistAdminController {

    private final ArtistService artistService;

    @PostMapping
    public ResponseEntity<ArtistAdminResponse> save(@RequestPart @Valid ArtistSaveRequest artistSaveRequest,
                                                    @RequestPart(required = false) MultipartFile profileImage,
                                                    @RequestPart(required = false) List<MultipartFile> assets) {
        return ResponseEntity.ok(artistService.saveArtist(artistSaveRequest, profileImage, assets));
    }

    @PutMapping("/{artistId}")
    public ResponseEntity<ArtistAdminResponse> update(@PathVariable UUID artistId,
                                                      @RequestBody @Valid ArtistUpdateRequest artistUpdateRequest) {
        return ResponseEntity.ok(artistService.updateArtist(artistId, artistUpdateRequest));
    }

    @DeleteMapping("/{artistId}")
    public ResponseEntity<Object> delete(@PathVariable UUID artistId) {
        artistService.deleteArtist(artistId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{artistId}")
    public ResponseEntity<ArtistAdminResponse> getArtist(@PathVariable UUID artistId) {
        return ResponseEntity.ok(artistService.adminGetArtistById(artistId));
    }

    @GetMapping
    public ResponseEntity<Page<ArtistAdminListResponse>> getAllArtists(Pageable pageable) {
        return ResponseEntity.ok(artistService.adminGetAllArtists(pageable));
    }

    @GetMapping("/name")
    public ResponseEntity<List<ArtistAdminNameResponse>> getAllArtistNames() {
        return ResponseEntity.ok(artistService.adminGetArtistNames());
    }
}
