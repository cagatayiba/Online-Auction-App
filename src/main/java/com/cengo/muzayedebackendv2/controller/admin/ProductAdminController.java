package com.cengo.muzayedebackendv2.controller.admin;

import com.cengo.muzayedebackendv2.domain.request.ProductSaveRequest;
import com.cengo.muzayedebackendv2.domain.request.ProductUpdateRequest;
import com.cengo.muzayedebackendv2.domain.response.admin.ProductAdminResponse;
import com.cengo.muzayedebackendv2.security.permission.AdminRole;
import com.cengo.muzayedebackendv2.service.ProductService;
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
@RequestMapping("/api/v1/admin/products")
@RequiredArgsConstructor
public class ProductAdminController {

    private final ProductService productService;

    @PostMapping
    public ResponseEntity<ProductAdminResponse> save(@RequestPart @Valid ProductSaveRequest productSaveRequest,
                                                     @RequestPart(required = false) MultipartFile coverImage,
                                                     @RequestPart(required = false) List<MultipartFile> assets) {
        return ResponseEntity.ok(productService.saveProduct(productSaveRequest, coverImage, assets));
    }

    @PostMapping("/{productId}/decision")
    public ResponseEntity<ProductAdminResponse> decideSale(@PathVariable UUID productId,
                                                            @RequestParam Boolean decision) {
        return ResponseEntity.ok(productService.decideSale(productId, decision));
    }

    @PutMapping("/{productId}")
    public ResponseEntity<ProductAdminResponse> update(@PathVariable UUID productId,
                                                       @RequestBody @Valid ProductUpdateRequest productUpdateRequest) {
        return ResponseEntity.ok(productService.updateProduct(productId, productUpdateRequest));
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<Object> delete(@PathVariable UUID productId) {
        productService.deleteProductById(productId);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<Page<ProductAdminResponse>> getAllProducts(Pageable pageable) {
        return ResponseEntity.ok(productService.adminGetAllProducts(pageable));
    }

    @GetMapping("/requests")
    public ResponseEntity<List<ProductAdminResponse>> getRequestProducts() {
        return ResponseEntity.ok(productService.adminGetRequestProducts());
    }

    @GetMapping("/{productId}")
    public ResponseEntity<ProductAdminResponse> getProduct(@PathVariable UUID productId) {
        return ResponseEntity.ok(productService.adminGetProductById(productId));
    }
}
