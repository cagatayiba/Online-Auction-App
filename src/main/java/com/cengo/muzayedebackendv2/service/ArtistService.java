package com.cengo.muzayedebackendv2.service;

import com.cengo.muzayedebackendv2.domain.dto.ArtistNameDTO;
import com.cengo.muzayedebackendv2.domain.dto.SaveAssetDTO;
import com.cengo.muzayedebackendv2.domain.dto.SaveVideoDTO;
import com.cengo.muzayedebackendv2.domain.entity.Artist;
import com.cengo.muzayedebackendv2.domain.entity.enums.AssetDomainType;
import com.cengo.muzayedebackendv2.domain.request.ArtistDescriptionVideoRequest;
import com.cengo.muzayedebackendv2.domain.request.ArtistSaveRequest;
import com.cengo.muzayedebackendv2.domain.request.ArtistUpdateRequest;
import com.cengo.muzayedebackendv2.domain.response.AssetResponse;
import com.cengo.muzayedebackendv2.domain.response.admin.ArtistAdminListResponse;
import com.cengo.muzayedebackendv2.domain.response.admin.ArtistAdminNameResponse;
import com.cengo.muzayedebackendv2.domain.response.admin.ArtistAdminResponse;
import com.cengo.muzayedebackendv2.domain.response.artist.ArtistListResponse;
import com.cengo.muzayedebackendv2.domain.response.artist.ArtistLotResponse;
import com.cengo.muzayedebackendv2.domain.response.artist.ArtistResponse;
import com.cengo.muzayedebackendv2.domain.response.artist.ArtistVideoResponse;
import com.cengo.muzayedebackendv2.mapper.ArtistMapper;
import com.cengo.muzayedebackendv2.repository.ArtistRepository;
import com.cengo.muzayedebackendv2.service.base.BaseEntityService;
import com.cengo.muzayedebackendv2.service.video.ArtistBiographyVideoService;
import com.cengo.muzayedebackendv2.util.rsql.RSQLSpecificationConverter;
import com.cengo.muzayedebackendv2.validation.artist.ArtistDeleteValidation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;
import java.util.stream.IntStream;
import java.util.stream.Stream;

@Service
public class ArtistService extends BaseEntityService<Artist, ArtistRepository> {

    private final AssetService assetService;
    private final ArtistBiographyVideoService videoService;
    private final LotService lotService;
    private final ArtistMapper artistMapper;
    private final ProductService productService;
    private final ArtistDeleteValidation artistIsDeletableValidation;
    private final RSQLSpecificationConverter rsqlConverter;

    protected ArtistService(ArtistRepository repository, AssetService assetService, ArtistBiographyVideoService videoService, ArtistMapper artistMapper, LotService lotService, ProductService productService, ArtistDeleteValidation artistIsDeletableValidation, RSQLSpecificationConverter rsqlConverter) {
        super(repository);
        this.assetService = assetService;
        this.videoService = videoService;
        this.lotService = lotService;
        this.artistMapper = artistMapper;
        this.productService = productService;
        this.artistIsDeletableValidation = artistIsDeletableValidation;
        this.rsqlConverter = rsqlConverter;
    }

    @Transactional
    public ArtistAdminResponse saveArtist(ArtistSaveRequest request, MultipartFile profileImage, List<MultipartFile> images) {
        Artist artist = save(artistMapper.convertToArtist(request));

        images = images == null ? Stream.of((MultipartFile) null).toList() : images;

        var profileImageAsset = saveArtistProfileImage(profileImage, artist.getId());
        var workImageAssets = saveArtistWorkImages(images, artist.getId());
        var videos = saveArtistVideos(request.descriptionVideos(), artist.getId());

        return artistMapper.convertToArtistAdminResponse(artist, profileImageAsset, workImageAssets, videos);
    }

    @Transactional
    public ArtistAdminResponse updateArtist(UUID artistId, ArtistUpdateRequest request) {
        Artist artist = getById(artistId);
        artistMapper.updateArtist(artist, request);
        save(artist);

        videoService.deleteVideosByArtistId(artistId);
        saveArtistVideos(request.descriptionVideos(), artist.getId());
        var profileImageAsset = assetService.getAssetByReferenceIdAndType(artist.getId(), AssetDomainType.ARTIST_PROFILE);
        var workImageAssets = assetService.getAssetsByReferenceIdAndType(artistId, AssetDomainType.ARTIST_WORK);
        var videos = videoService.getVideosByArtistId(artistId);

        return artistMapper.convertToArtistAdminResponse(artist, profileImageAsset, workImageAssets, videos);
    }

    @Transactional
    public void deleteArtist(UUID artistId) {
        var artist = getById(artistId);
        var lots = lotService.findLotsByArtistId(artistId);
        var products = productService.findProductsByArtistId(artistId);

        artistIsDeletableValidation.validate(new ArtistDeleteValidation.Context(lots, products));

        lotService.deleteAllLots(lots);
        productService.deleteAllProducts(products);
        assetService.deleteAllByReferenceId(artistId);
        videoService.deleteVideosByArtistId(artistId);
        delete(artist);
    }

    public ArtistAdminResponse adminGetArtistById(UUID artistId) {
        Artist artist = getById(artistId);
        var profileImageAsset = assetService.getAssetByReferenceIdAndType(artistId, AssetDomainType.ARTIST_PROFILE);
        var workImageAssets = assetService.getAssetsByReferenceIdAndType(artistId, AssetDomainType.ARTIST_WORK);
        var videos = videoService.getVideosByArtistId(artistId);

        return artistMapper.convertToArtistAdminResponse(artist, profileImageAsset, workImageAssets, videos);
    }

    public Page<ArtistAdminListResponse> adminGetAllArtists(Pageable pageable) {
        Page<Artist> artists = findAll(pageable);

        return artists.map(artist -> {
            var profileImageAsset = assetService.getAssetByReferenceIdAndType(artist.getId(), AssetDomainType.ARTIST_PROFILE);
            return artistMapper.convertToArtistAdminListResponse(artist, profileImageAsset);
        });
    }

    @Transactional
    public List<ArtistAdminNameResponse> adminGetArtistNames() {
        // TODO bunu dto'dan alalım
        var artists = getRepository().findAllByOrderByFullName();
        return artists.stream().map(artistMapper::convertToArtistNameResponse).toList();
    }

    public ArtistResponse getArtistById(UUID artistId) {
        Artist artist = getById(artistId);
        var profileImageAsset = assetService.getAssetByReferenceIdAndType(artistId, AssetDomainType.ARTIST_PROFILE);
        var workImageAssets = assetService.getAssetsByReferenceIdAndType(artistId, AssetDomainType.ARTIST_WORK);
        var videos = videoService.getVideosByArtistId(artistId);

        return artistMapper.convertToArtistResponse(artist, profileImageAsset, workImageAssets, videos);
    }

    public Page<ArtistListResponse> getAllArtists(Pageable pageable, String filter) {
        Specification<Artist> spec = rsqlConverter.convertToSpec(filter);
        Page<Artist> artists = getRepository().findAll(spec, pageable);

        return artists.map(artist -> {
            var profileImageAsset = assetService.getAssetByReferenceIdAndType(artist.getId(), AssetDomainType.ARTIST_PROFILE);
            return artistMapper.convertToArtistListResponse(artist, profileImageAsset);
        });
    }

    public List<ArtistNameDTO> getArtistNameInfo(Iterable<UUID> artistIds) {
        return getRepository().findNameInfoByIdsOrderByName(artistIds);
    }

    public ArtistLotResponse getArtistInfo(UUID artistId) {
        Artist artist = getById(artistId);
        var profileImage = assetService.getAssetByReferenceIdAndType(artistId, AssetDomainType.ARTIST_PROFILE);

        return artistMapper.convertToArtistLotResponse(artist, profileImage);
    }

    private AssetResponse saveArtistProfileImage(MultipartFile profileImage, UUID artistId) {
        return assetService.saveAsset(
                SaveAssetDTO.builder()
                        .referenceId(artistId)
                        .file(profileImage)
                        .domainType(AssetDomainType.ARTIST_PROFILE)
                        .build()
        );
    }

    private List<AssetResponse> saveArtistWorkImages(List<MultipartFile> images, UUID artistId) {
        List<SaveAssetDTO> assetsToSave = IntStream.range(0, images.size())
                .mapToObj(index ->
                        SaveAssetDTO.builder()
                                .referenceId(artistId)
                                .file(images.get(index))
                                .domainType(AssetDomainType.ARTIST_WORK)
                                .orderNo(index + 1)
                                .build()
                ).toList();

        return assetService.saveAssets(assetsToSave);
    }

    private List<ArtistVideoResponse> saveArtistVideos(List<ArtistDescriptionVideoRequest> videos, UUID artistId) {
        List<SaveVideoDTO> videosToSave = IntStream.range(0, videos.size())
                .mapToObj(index ->
                        SaveVideoDTO.builder()
                                .artistId(artistId)
                                .header(videos.get(index).header())
                                .url(videos.get(index).url())
                                .order(index + 1)
                                .build()
                ).toList();

        return videoService.saveVideos(videosToSave);
    }
}
