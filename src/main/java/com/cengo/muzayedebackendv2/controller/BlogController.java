package com.cengo.muzayedebackendv2.controller;

import com.cengo.muzayedebackendv2.domain.response.blog.BlogResponse;
import com.cengo.muzayedebackendv2.security.permission.PermitAll;
import com.cengo.muzayedebackendv2.service.BlogService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/blogs")
@RequiredArgsConstructor
public class BlogController {

    private final BlogService blogService;

    @PermitAll
    @GetMapping
    public ResponseEntity<Page<BlogResponse>> getAllBlogs(Pageable pageable) {
        return ResponseEntity.ok(blogService.getAllBlogs(pageable));
    }

    @PermitAll
    @GetMapping("/{blogId}")
    public ResponseEntity<BlogResponse> getBlog(@PathVariable UUID blogId) {
        return ResponseEntity.ok(blogService.getBlogById(blogId));
    }
}
