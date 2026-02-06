package com.cengo.muzayedebackendv2.controller;

import com.cengo.muzayedebackendv2.domain.response.artist.ArtistListProductResponse;
import com.cengo.muzayedebackendv2.domain.response.product.ProductListResponse;
import com.cengo.muzayedebackendv2.domain.response.product.ProductResponse;
import com.cengo.muzayedebackendv2.security.UserPrincipal;
import com.cengo.muzayedebackendv2.security.permission.PermitAll;
import com.cengo.muzayedebackendv2.security.permission.UserRole;
import com.cengo.muzayedebackendv2.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;


@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    @PermitAll
    @GetMapping
    public ResponseEntity<Page<ProductListResponse>> getAllProducts(@RequestParam(required = false) String filter, Pageable pageable) {
        return ResponseEntity.ok(productService.getAllProducts(pageable, filter));
    }

    @PermitAll
    @GetMapping("/artists")
    public ResponseEntity<List<ArtistListProductResponse>> getAllProductArtists() {
        return ResponseEntity.ok(productService.getArtistsOfProducts());
    }

    @PermitAll
    @GetMapping("/{productId}")
    public ResponseEntity<ProductResponse> getProduct(@PathVariable UUID productId) {
        return ResponseEntity.ok(productService.getProductById(productId));
    }

    @UserRole
    @PostMapping ("/buy/{productId}")
    public ResponseEntity<Object> buyProduct(@AuthenticationPrincipal UserPrincipal user,
                                             @PathVariable UUID productId) {
        productService.buyProduct(productId, user.getUserId());
        return ResponseEntity.ok().build();
    }

}
