package com.cengo.muzayedebackendv2.controller.admin;

import com.cengo.muzayedebackendv2.domain.request.BlogSaveRequest;
import com.cengo.muzayedebackendv2.domain.request.BlogUpdateRequest;
import com.cengo.muzayedebackendv2.domain.response.admin.*;
import com.cengo.muzayedebackendv2.security.permission.AdminRole;
import com.cengo.muzayedebackendv2.service.BlogService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@AdminRole
@RestController
@RequestMapping("/api/v1/admin/blogs")
@RequiredArgsConstructor
public class BlogAdminController {

    private final BlogService blogService;

    @PostMapping
    public ResponseEntity<BlogAdminResponse> save(@RequestPart @Valid BlogSaveRequest blogSaveRequest,
                                                  @RequestPart(required = false) MultipartFile coverImage) {
        return ResponseEntity.ok(blogService.saveBlog(blogSaveRequest, coverImage));
    }

    @PutMapping("/{blogId}")
    public ResponseEntity<BlogAdminResponse> update(@PathVariable UUID blogId,
                                                    @RequestBody @Valid BlogUpdateRequest blogUpdateRequest) {
        return ResponseEntity.ok(blogService.updateBlog(blogId, blogUpdateRequest));
    }

    @DeleteMapping("/{blogId}")
    public ResponseEntity<Object> delete(@PathVariable UUID blogId) {
        blogService.deleteBlog(blogId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{blogId}")
    public ResponseEntity<BlogAdminResponse> getBlog(@PathVariable UUID blogId) {
        return ResponseEntity.ok(blogService.adminGetBlogById(blogId));
    }

    @GetMapping
    public ResponseEntity<Page<BlogAdminListResponse>> getAllBlogs(Pageable pageable) {
        return ResponseEntity.ok(blogService.adminGetAllBlogs(pageable));
    }
}
