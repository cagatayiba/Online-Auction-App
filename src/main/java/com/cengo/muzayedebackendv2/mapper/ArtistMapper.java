package com.cengo.muzayedebackendv2.mapper;

import com.cengo.muzayedebackendv2.domain.entity.Artist;
import com.cengo.muzayedebackendv2.domain.request.ArtistSaveRequest;
import com.cengo.muzayedebackendv2.domain.request.ArtistUpdateRequest;
import com.cengo.muzayedebackendv2.domain.response.*;
import com.cengo.muzayedebackendv2.domain.response.admin.ArtistAdminListResponse;
import com.cengo.muzayedebackendv2.domain.response.admin.ArtistAdminResponse;
import com.cengo.muzayedebackendv2.domain.response.admin.ArtistAdminNameResponse;
import com.cengo.muzayedebackendv2.domain.response.artist.*;
import org.mapstruct.*;

import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface ArtistMapper {

    @Mapping(target = "summary", source = "summary", qualifiedByName = "mapStringToByteArray")
    @Mapping(target = "biography", source = "biography", qualifiedByName = "mapStringToByteArray")
    Artist convertToArtist(ArtistSaveRequest request);

    @Mapping(target = "summary", source = "artist.summary", qualifiedByName = "mapByteArrayToString")
    @Mapping(target = "biography", source = "artist.biography", qualifiedByName = "mapByteArrayToString")
    ArtistResponse convertToArtistResponse(Artist artist, AssetResponse profileImage,
                                           List<AssetResponse> workAssets, List<ArtistVideoResponse> videoUrls);

    @Mapping(target = "id", source = "artist.id")
    ArtistListResponse convertToArtistListResponse(Artist artist, AssetResponse profileImage);

    @Mapping(target = "id", source = "artist.id")
    @Mapping(target = "summary", source = "artist.summary", qualifiedByName = "mapByteArrayToString")
    @Mapping(target = "biography", source = "artist.biography", qualifiedByName = "mapByteArrayToString")
    ArtistAdminResponse convertToArtistAdminResponse(Artist artist, AssetResponse profileImage,
                                                     List<AssetResponse> workAssets, List<ArtistVideoResponse> videoUrls);

    @Mapping(target = "id", source = "artist.id")
    ArtistAdminListResponse convertToArtistAdminListResponse(Artist artist, AssetResponse profileImage);

    ArtistAdminNameResponse convertToArtistNameResponse(Artist artist);

    @Mapping(target = "id", source = "artist.id")
    @Mapping(target = "summary", source = "artist.summary", qualifiedByName = "mapByteArrayToString")
    ArtistLotResponse convertToArtistLotResponse(Artist artist, AssetResponse profileImage);

    @Mapping(target = "summary", source = "summary", qualifiedByName = "mapStringToByteArray")
    @Mapping(target = "biography", source = "biography", qualifiedByName = "mapStringToByteArray")
    void updateArtist(@MappingTarget Artist artist, ArtistUpdateRequest artistUpdateRequest);


    @Named("mapByteArrayToString")
    default String mapInfo(byte[] source) {
        return new String(source);
    }

    @Named("mapStringToByteArray")
    default byte[] mapInfo(String source) {
        return source.getBytes();
    }

}
