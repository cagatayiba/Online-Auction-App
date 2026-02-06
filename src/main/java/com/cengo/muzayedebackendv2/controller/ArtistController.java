package com.cengo.muzayedebackendv2.controller;

import com.cengo.muzayedebackendv2.domain.response.artist.ArtistListResponse;
import com.cengo.muzayedebackendv2.domain.response.artist.ArtistResponse;
import com.cengo.muzayedebackendv2.security.permission.PermitAll;
import com.cengo.muzayedebackendv2.service.ArtistService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/artists")
@RequiredArgsConstructor
public class ArtistController {

    private final ArtistService artistService;

    @PermitAll
    @GetMapping
    public ResponseEntity<Page<ArtistListResponse>> getAllArtists(Pageable pageable, @RequestParam(required = false) String filter) {
        return ResponseEntity.ok(artistService.getAllArtists(pageable, filter));
    }

    @PermitAll
    @GetMapping("/{artistId}")
    public ResponseEntity<ArtistResponse> getArtist(@PathVariable UUID artistId) {
        return ResponseEntity.ok(artistService.getArtistById(artistId));
    }
}
