package com.cengo.muzayedebackendv2.mapper;

import com.cengo.muzayedebackendv2.domain.dto.SaveAssetDTO;
import com.cengo.muzayedebackendv2.domain.entity.Asset;
import com.cengo.muzayedebackendv2.domain.entity.enums.AssetMimeType;
import com.cengo.muzayedebackendv2.domain.response.AssetResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface AssetMapper {

    @Mapping(target = "mimeType", source = "file", qualifiedByName = "mapMimeType")
    Asset convertToAsset(SaveAssetDTO saveRequest);

    @Mapping(target = "type", source = "mimeType")
    AssetResponse convertToAssetResponse(Asset asset);

    List<AssetResponse> convertToAssetResponses(List<Asset> assets);


    @Named("mapMimeType")
    default AssetMimeType mapMimeType(MultipartFile file) {
        return AssetMimeType.fromFile(file);
    }
}
