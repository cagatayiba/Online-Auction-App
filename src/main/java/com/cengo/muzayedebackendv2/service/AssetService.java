package com.cengo.muzayedebackendv2.service;

import com.cengo.muzayedebackendv2.config.properties.AppProperties;
import com.cengo.muzayedebackendv2.domain.dto.SaveAssetDTO;
import com.cengo.muzayedebackendv2.domain.entity.Asset;
import com.cengo.muzayedebackendv2.domain.entity.enums.AssetDomainType;
import com.cengo.muzayedebackendv2.domain.entity.enums.AssetMimeType;
import com.cengo.muzayedebackendv2.domain.response.AssetResponse;
import com.cengo.muzayedebackendv2.mapper.AssetMapper;
import com.cengo.muzayedebackendv2.repository.*;
import com.cengo.muzayedebackendv2.service.base.BaseEntityService;
import com.cengo.muzayedebackendv2.service.file.FileService;
import com.cengo.muzayedebackendv2.validation.asset.AssetChangeOrderValidation;
import com.cengo.muzayedebackendv2.validation.asset.AssetDeleteValidation;
import com.cengo.muzayedebackendv2.validation.asset.AssetSaveValidation;
import com.cengo.muzayedebackendv2.validation.asset.AssetUpdateValidation;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;
import java.util.stream.IntStream;

@Service
@Validated
public class AssetService extends BaseEntityService<Asset, AssetRepository> {
    private final FileService fileService;
    private final AssetMapper assetMapper;

    private final AppProperties appProperties;
    private final AssetSaveValidation assetSaveValidation;
    private final AssetUpdateValidation assetUpdateValidation;
    private final AssetChangeOrderValidation assetChangeOrderValidation;
    private final AssetDeleteValidation assetDeleteValidation;

    protected AssetService(AssetRepository repository, FileService fileService, AssetMapper assetMapper, AppProperties appProperties, AssetSaveValidation assetSaveValidation, AssetUpdateValidation assetUpdateValidation, AssetChangeOrderValidation assetChangeOrderValidation, AssetDeleteValidation assetDeleteValidation) {
        super(repository);
        this.fileService = fileService;
        this.assetMapper = assetMapper;
        this.appProperties = appProperties;
        this.assetSaveValidation = assetSaveValidation;
        this.assetUpdateValidation = assetUpdateValidation;
        this.assetChangeOrderValidation = assetChangeOrderValidation;
        this.assetDeleteValidation = assetDeleteValidation;
    }

    @Transactional
    public AssetResponse saveAssetRequest(UUID referenceId, MultipartFile file) {
        var context = new AssetSaveValidation.Context(referenceId, null);
        assetSaveValidation.validate(context);

        return saveAsset(SaveAssetDTO.builder()
                .referenceId(referenceId)
                .domainType(context.getAssetDomainType())
                .file(file)
                .orderNo(getAssetsByReferenceIdAndType(referenceId, context.getAssetDomainType()).size() + 1)
                .build());
    }

    @Transactional
    public List<AssetResponse> saveAssets(@Valid List<SaveAssetDTO> saveRequests) {
        return saveRequests.stream().map(this::saveAsset).toList();
    }

    public AssetResponse saveAsset(@Valid SaveAssetDTO saveRequest) {
        Asset asset = save(assetMapper.convertToAsset(saveRequest));
        String url;
        String alt = null;
        if (asset.getMimeType() == AssetMimeType.DEFAULT) {
            url = appProperties.awsBaseUrl() + "default/" + asset.getDomainType().name();
            asset.setMimeType(AssetMimeType.IMAGE);
        }else {
            url = uploadFile(asset.getId(), saveRequest.file());
            alt = saveRequest.file().getOriginalFilename() + " - UpArt";
        }
        asset.setUrl(url);
        asset.setAlt(alt);

        return assetMapper.convertToAssetResponse(save(asset));
    }

    public AssetResponse getAssetByReferenceIdAndType(UUID referenceId, AssetDomainType type) {
        if (type == AssetDomainType.AUCTION_COVER_IMAGE) {
            return getAssetByReferenceId(referenceId);
        }
        return assetMapper.convertToAssetResponse(getRepository().findByReferenceIdAndDomainType(referenceId, type));
    }

    public List<AssetResponse> getAssetsByReferenceIdAndType(UUID referenceId, AssetDomainType type) {
        return assetMapper.convertToAssetResponses(getRepository().findAllByReferenceIdAndDomainTypeOrderByOrderNo(referenceId, type));
    }

    @Transactional
    public AssetResponse updateAsset(UUID assetId, MultipartFile file) {
        var deletedAsset = getById(assetId);

        assetUpdateValidation.validate(new AssetUpdateValidation.Context(deletedAsset));

        fileService.deleteFile(String.valueOf(assetId));
        delete(deletedAsset);

        var request = SaveAssetDTO.builder()
                .referenceId(deletedAsset.getReferenceId())
                .domainType(deletedAsset.getDomainType())
                .orderNo(deletedAsset.getOrderNo())
                .file(file)
                .build();

        return saveAsset(request);
    }

    @Transactional
    public void deleteAllByReferenceId(UUID referenceId) {
        List<Asset> assets = getRepository().findAllByReferenceId(referenceId);
        assets.stream().map(asset -> String.valueOf(asset.getId())).toList().forEach(fileService::deleteFile);

        deleteAll(assets);
    }

    @Transactional
    public void deleteAsset(UUID assetId) {
        var asset = getById(assetId);

        assetDeleteValidation.validate(new AssetDeleteValidation.Context(asset));

        var referenceAssets = getRepository()
                .findAllByReferenceIdAndDomainTypeOrderByOrderNo(asset.getReferenceId(), asset.getDomainType())
                .stream()
                .filter(refAsset -> refAsset.getId() != assetId)
                .toList();

        IntStream.range(0, referenceAssets.size())
                .forEach(index -> referenceAssets.get(index).setOrderNo(index + 1));
        saveAll(referenceAssets);

        fileService.deleteFile(String.valueOf(assetId));
        delete(asset);
    }

    public List<AssetResponse> changeOrder(List<UUID> ids) {
        var asset1 = getById(ids.get(0));
        var asset2 = getById(ids.get(1));

        assetChangeOrderValidation.validate(new AssetChangeOrderValidation.Context(asset1, asset2));

        int tmpOrderNo = asset1.getOrderNo();
        asset1.setOrderNo(asset2.getOrderNo());
        asset2.setOrderNo(tmpOrderNo);

        var assets = saveAll(List.of(asset1, asset2));
        return assets.stream().map(assetMapper::convertToAssetResponse).toList();
    }


    private AssetResponse getAssetByReferenceId(UUID referenceId) {
        return assetMapper.convertToAssetResponse(getRepository().findByReferenceId(referenceId));
    }

    private String uploadFile(UUID assetId, MultipartFile file) {
        String fileKey = String.valueOf(assetId);
        fileService.uploadFile(file, fileKey);
        return appProperties.awsBaseUrl() + fileKey;
    }
}
