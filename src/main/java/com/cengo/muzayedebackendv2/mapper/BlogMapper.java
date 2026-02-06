package com.cengo.muzayedebackendv2.mapper;

import com.cengo.muzayedebackendv2.domain.entity.Blog;
import com.cengo.muzayedebackendv2.domain.request.BlogSaveRequest;
import com.cengo.muzayedebackendv2.domain.request.BlogUpdateRequest;
import com.cengo.muzayedebackendv2.domain.response.AssetResponse;
import com.cengo.muzayedebackendv2.domain.response.admin.BlogAdminListResponse;
import com.cengo.muzayedebackendv2.domain.response.admin.BlogAdminResponse;
import com.cengo.muzayedebackendv2.domain.response.blog.BlogResponse;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface BlogMapper {

    @Mapping(target = "content", source = "content", qualifiedByName = "mapStringToByteArray")
    Blog convertToBlog(BlogSaveRequest request);

    @Mapping(target = "content", source = "content", qualifiedByName = "mapStringToByteArray")
    void updateBlog(@MappingTarget Blog blog, BlogUpdateRequest request);

    @Mapping(target = "id", source = "blog.id")
    @Mapping(target = "content", source = "blog.content", qualifiedByName = "mapByteArrayToString")
    BlogResponse convertToBlogResponse(Blog blog, AssetResponse coverImage);

    @Mapping(target = "id", source = "blog.id")
    @Mapping(target = "content", source = "blog.content", qualifiedByName = "mapByteArrayToString")
    BlogAdminResponse convertToBlogAdminResponse(Blog blog, AssetResponse coverImage);

    @Mapping(target = "id", source = "blog.id")
    BlogAdminListResponse convertToBlogAdminListResponse(Blog blog, AssetResponse coverImage);


    @Named("mapByteArrayToString")
    default String mapInfo(byte[] source) {
        return new String(source);
    }

    @Named("mapStringToByteArray")
    default byte[] mapInfo(String source) {
        return source.getBytes();
    }
}
