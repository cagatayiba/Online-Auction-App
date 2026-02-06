package com.cengo.muzayedebackendv2.service;

import com.cengo.muzayedebackendv2.domain.dto.SaveAssetDTO;
import com.cengo.muzayedebackendv2.domain.entity.Blog;
import com.cengo.muzayedebackendv2.domain.entity.enums.AssetDomainType;
import com.cengo.muzayedebackendv2.domain.request.BlogSaveRequest;
import com.cengo.muzayedebackendv2.domain.request.BlogUpdateRequest;
import com.cengo.muzayedebackendv2.domain.response.AssetResponse;
import com.cengo.muzayedebackendv2.domain.response.admin.BlogAdminListResponse;
import com.cengo.muzayedebackendv2.domain.response.admin.BlogAdminResponse;
import com.cengo.muzayedebackendv2.domain.response.blog.BlogResponse;
import com.cengo.muzayedebackendv2.mapper.BlogMapper;
import com.cengo.muzayedebackendv2.repository.BlogRepository;
import com.cengo.muzayedebackendv2.service.base.BaseEntityService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Service
public class BlogService extends BaseEntityService<Blog, BlogRepository> {

    private final BlogMapper blogMapper;
    private final AssetService assetService;

    protected BlogService(BlogRepository repository, BlogMapper blogMapper, AssetService assetService) {
        super(repository);
        this.blogMapper = blogMapper;
        this.assetService = assetService;
    }

    @Transactional
    public BlogAdminResponse saveBlog(BlogSaveRequest request, MultipartFile coverImage) {
        Blog blog = save(blogMapper.convertToBlog(request));

        var coverImageAsset = saveBlogCoverImage(coverImage, blog.getId());

        return blogMapper.convertToBlogAdminResponse(blog, coverImageAsset);
    }

    @Transactional
    public BlogAdminResponse updateBlog(UUID blogId, BlogUpdateRequest request) {
        Blog blog = getById(blogId);
        blogMapper.updateBlog(blog, request);

        var coverImage = assetService.getAssetByReferenceIdAndType(blogId, AssetDomainType.BLOG_COVER);

        return blogMapper.convertToBlogAdminResponse(save(blog), coverImage);
    }

    @Transactional
    public void deleteBlog(UUID blogId) {
        Blog blog = getById(blogId);
        delete(blog);
    }

    public BlogAdminResponse adminGetBlogById(UUID blogId) {
        Blog blog = getById(blogId);
        var coverImage = assetService.getAssetByReferenceIdAndType(blogId, AssetDomainType.BLOG_COVER);

        return blogMapper.convertToBlogAdminResponse(blog, coverImage);
    }

    public Page<BlogAdminListResponse> adminGetAllBlogs(Pageable pageable) {
        Page<Blog> blogs = findAll(pageable);
        return blogs.map(blog -> {
            var coverImage = assetService.getAssetByReferenceIdAndType(blog.getId(), AssetDomainType.BLOG_COVER);
            return blogMapper.convertToBlogAdminListResponse(blog, coverImage);
        });
    }

    public Page<BlogResponse> getAllBlogs(Pageable pageable) {
        Page<Blog> blogs = findAll(pageable);
        return blogs.map(blog -> {
            var coverImage = assetService.getAssetByReferenceIdAndType(blog.getId(), AssetDomainType.BLOG_COVER);
            return blogMapper.convertToBlogResponse(blog, coverImage);
        });
    }

    public BlogResponse getBlogById(UUID blogId) {
        Blog blog = getById(blogId);

        var coverImage = assetService.getAssetByReferenceIdAndType(blogId, AssetDomainType.BLOG_COVER);

        return blogMapper.convertToBlogResponse(blog, coverImage);
    }

    private AssetResponse saveBlogCoverImage(MultipartFile coverImage, UUID id) {
        return assetService.saveAsset(
                SaveAssetDTO.builder()
                        .referenceId(id)
                        .file(coverImage)
                        .domainType(AssetDomainType.BLOG_COVER)
                        .build()
        );
    }
}
