package com.cengo.muzayedebackendv2.service;

import com.cengo.muzayedebackendv2.domain.dto.SaveAssetDTO;
import com.cengo.muzayedebackendv2.domain.entity.Auction;
import com.cengo.muzayedebackendv2.domain.entity.Lot;
import com.cengo.muzayedebackendv2.domain.entity.enums.AssetDomainType;
import com.cengo.muzayedebackendv2.domain.entity.enums.AuctionState;
import com.cengo.muzayedebackendv2.domain.request.AuctionSaveRequest;
import com.cengo.muzayedebackendv2.domain.request.AuctionUpdateRequest;
import com.cengo.muzayedebackendv2.domain.response.AssetResponse;
import com.cengo.muzayedebackendv2.domain.response.admin.*;
import com.cengo.muzayedebackendv2.domain.response.artist.ArtistListAuctionResponse;
import com.cengo.muzayedebackendv2.domain.response.auction.AuctionListResponse;
import com.cengo.muzayedebackendv2.domain.response.auction.AuctionResponse;
import com.cengo.muzayedebackendv2.domain.response.auction.AuctionWinningResponse;
import com.cengo.muzayedebackendv2.exception.ItemNotFoundException;
import com.cengo.muzayedebackendv2.exception.message.ErrorMessage;
import com.cengo.muzayedebackendv2.mapper.AuctionMapper;
import com.cengo.muzayedebackendv2.repository.AuctionRepository;
import com.cengo.muzayedebackendv2.security.UserPrincipal;
import com.cengo.muzayedebackendv2.service.base.BaseEntityService;
import com.cengo.muzayedebackendv2.validation.auction.AuctionUpdateValidation;
import com.cengo.muzayedebackendv2.validation.auction.AuctionSetDraftValidation;
import com.cengo.muzayedebackendv2.validation.auction.AuctionSetReadyValidation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class AuctionService extends BaseEntityService<Auction, AuctionRepository> {
    private static final String CURRENT_AUCTION_INFO_TEXT = "Son teklifler";
    private static final String INCOMING_AUCTION_INFO_TEXT = "Müzayede başlıyor";

    private final AssetService assetService;
    private final LotService lotService;
    private final ArtistService artistService;
    private final AuctionMapper auctionMapper;
    private final AuctionSetReadyValidation auctionSetReadyValidation;
    private final AuctionSetDraftValidation auctionSetDraftValidation;
    private final AuctionUpdateValidation auctionIsUpdatableValidation;


    protected AuctionService(AuctionRepository repository, AssetService assetService, AuctionMapper auctionMapper, LotService lotService, ArtistService artistService, AuctionSetReadyValidation auctionSetReadyValidation, AuctionSetDraftValidation auctionSetDraftValidation, AuctionUpdateValidation auctionIsUpdatableValidation) {
        super(repository);
        this.assetService = assetService;
        this.lotService = lotService;
        this.auctionMapper = auctionMapper;
        this.artistService = artistService;
        this.auctionSetReadyValidation = auctionSetReadyValidation;
        this.auctionSetDraftValidation = auctionSetDraftValidation;
        this.auctionIsUpdatableValidation = auctionIsUpdatableValidation;
    }

    @Transactional
    public AuctionAdminResponse saveAuction(AuctionSaveRequest auctionSaveRequest, MultipartFile coverImage) {
        Auction auction = save(auctionMapper.convertToAuction(auctionSaveRequest));

        SaveAssetDTO dto = SaveAssetDTO.builder()
                .referenceId(auction.getId())
                .domainType(AssetDomainType.AUCTION_COVER_IMAGE)
                .file(coverImage)
                .build();
        AssetResponse asset = assetService.saveAsset(dto);

        return auctionMapper.convertToAuctionAdminResponse(auction, asset);
    }

    @Transactional
    public AuctionAdminResponse updateAuction(UUID auctionId, AuctionUpdateRequest auctionUpdateRequest) {
        Auction auction = getById(auctionId);

        auctionIsUpdatableValidation.validate(new AuctionUpdateValidation.Context(auction));

        auctionMapper.updateAuction(auction, auctionUpdateRequest);

        return getAuctionAdminResponse(save(auction));
    }

    @Transactional
    public void deleteAuction(UUID auctionId) {
        var auction = getById(auctionId);

        auctionIsUpdatableValidation.validate(new AuctionUpdateValidation.Context(auction));

        delete(auction);
        lotService.deleteAllByAuctionId(auctionId);
        assetService.deleteAllByReferenceId(auctionId);
    }

    @Transactional
    public void startAuction(UUID auctionId) {
        var auction = getById(auctionId);

        auction.setState(AuctionState.STARTED);
        lotService.startLots(auctionId);
        save(auction);
    }

    public void endAuction(UUID auctionId) {
        var auction = getById(auctionId);

        auction.setState(AuctionState.COMPLETED);
        save(auction);
        //TODO send email
    }

    @Transactional
    public AuctionAdminResponse setReady(UUID auctionId) {
        var auction = getById(auctionId);
        var lots = lotService.findLotsByAuctionId(auctionId);

        auctionSetReadyValidation.validate(new AuctionSetReadyValidation.Context(auction, lots));

        auction.setState(AuctionState.READY);
        setAuctionNo();

        lotService.setLotsReady(lots, auction.getEndTime());
        return getAuctionAdminResponse(save(auction));
    }

    @Transactional
    public AuctionAdminResponse setDraft(UUID auctionId) {
        Auction auction = getById(auctionId);

        auctionSetDraftValidation.validate(new AuctionSetDraftValidation.Context(auction));

        auction.setState(AuctionState.DRAFT);

        lotService.setLotsDraft(auctionId);
        return getAuctionAdminResponse(save(auction));
    }

    public Auction getAuctionById(UUID auctionId) {
        return getById(auctionId);
    }

    public Optional<Auction> findReadyAuction() {
        return getRepository().findByStateAndStartTimeIsBefore(AuctionState.READY, LocalDateTime.now());
    }

    public Optional<Auction> findCurrentAuction() {
        return getRepository().findByState(AuctionState.STARTED);
    }

    public List<AuctionAdminListResponse> adminGetReadyAuctions() {
        List<Auction> readyAuctions = getRepository().findAllByStateOrderByName(AuctionState.READY);

        return readyAuctions.stream().map(auctionMapper::convertToAuctionListResponse).toList();
    }

    public List<AuctionAdminListResponse> adminGetDraftAuctions() {
        List<Auction> draftAuctions = getRepository().findAllByStateOrderByName(AuctionState.DRAFT);

        return draftAuctions.stream().map(auctionMapper::convertToAuctionListResponse).toList();
    }

    public AuctionAdminResponse adminGetAuctionById(UUID auctionId) {
        Auction auction = getById(auctionId);
        return getAuctionAdminResponse(auction);
    }

    public Page<LotAdminResponse> adminGetAuctionLots(UUID auctionId, Pageable pageable) {
        return lotService.adminGetLotsByAuctionId(auctionId, pageable);
    }

    public Page<LotAdminSaleResponse> adminGetAuctionSales(UUID auctionId, Pageable pageable) {
        return lotService.adminGetSalesByAuctionId(auctionId, pageable);
    }

    public AuctionListResponse getCurrentAuctionResponse() {
        Auction auction = getRepository().findByState(AuctionState.STARTED)
                .orElseThrow(() -> new ItemNotFoundException(ErrorMessage.CURRENT_AUCTION_NOT_FOUND));

        return getCurrentAuctionListResponse(auction);
    }

    public Page<AuctionListResponse> getIncomingAuctions(Pageable pageable){
        Page<Auction> incomingAuctions = getRepository().findAllByStateOrderByEndTimeDesc(AuctionState.READY, pageable);

        return incomingAuctions.map(this::getIncomingAuctionListResponse);
    }

    public Page<AuctionListResponse> getPastAuctions(Pageable pageable) {
        Page<Auction> pastAuctions = getRepository().findAllByStateOrderByEndTimeDesc(AuctionState.COMPLETED, pageable);

        return pastAuctions.map(this::getPastAuctionListResponse);
    }

    public AuctionResponse getAuctionLots(UUID auctionId, UserPrincipal user, Pageable pageable) {
        var auction = getById(auctionId);
        var lots = lotService.getLotsByAuctionId(auction, user, pageable);

        return auctionMapper.convertToAuctionListResponse(lots);
    }

    public AuctionResponse getAuctionLotsV2(UUID auctionId, UserPrincipal user, String filter, Pageable pageable) {
        var lots = lotService.getLotsByAuctionIdV2(auctionId, user, filter, pageable);

        return auctionMapper.convertToAuctionListResponse(lots);
    }

    public List<ArtistListAuctionResponse> getArtistsOfAuction(UUID auctionId){
        // TODO bütün lotları memory'e getirmek günah
        var lots = lotService.getLotEntitiesByAuctionId(auctionId);
        var lotCountByArtistId = lots.stream()
                .collect(Collectors.groupingBy(
                        Lot::getArtistId,
                        Collectors.summingInt(lot -> 1)
                ));
        var artistNames = artistService.getArtistNameInfo(lotCountByArtistId.keySet());
        return artistNames.stream().map( artistNameDTO ->
                new ArtistListAuctionResponse(
                        artistNameDTO.getId(),
                        artistNameDTO.getFullName(),
                        lotCountByArtistId.get(artistNameDTO.getId())
                )
        ).toList();
    }

    public List<AuctionWinningResponse> getUserWinnings(UUID userId) {
        var auctions = getRepository().findAllById(lotService.getAuctionsByUserId(userId));
        return auctions.stream().map(auction -> {
            var coverImage = assetService.getAssetByReferenceIdAndType(auction.getId(), AssetDomainType.AUCTION_COVER_IMAGE);
            return auctionMapper.convertToAuctionWinningResponse(auction, coverImage);
        }).toList();
    }


    private AuctionAdminResponse getAuctionAdminResponse(Auction auction) {
        var coverAsset = assetService.getAssetByReferenceIdAndType(auction.getId(), AssetDomainType.AUCTION_COVER_IMAGE);
        return auctionMapper.convertToAuctionAdminResponse(auction, coverAsset);
    }

    private AuctionListResponse getCurrentAuctionListResponse(Auction auction){
        var coverImage = assetService.getAssetByReferenceIdAndType(auction.getId(), AssetDomainType.AUCTION_COVER_IMAGE);
        var remainingTime = (int) Math.max(LocalDateTime.now().until(auction.getEndTime(), ChronoUnit.MINUTES), 0);
        var infoText = remainingTime > 0 ? null : CURRENT_AUCTION_INFO_TEXT;

        return auctionMapper.convertToAuctionListResponse(auction, coverImage, remainingTime, infoText);
    }

    private AuctionListResponse getIncomingAuctionListResponse(Auction auction){
        var coverImage = assetService.getAssetByReferenceIdAndType(auction.getId(), AssetDomainType.AUCTION_COVER_IMAGE);
        var remainingTime = (int) Math.max(auction.getStartTime().until(LocalDateTime.now(), ChronoUnit.MINUTES), 0);
        var infoText = remainingTime > 0 ? null : INCOMING_AUCTION_INFO_TEXT;

        return auctionMapper.convertToAuctionListResponse(auction, coverImage, remainingTime, infoText);
    }

    private AuctionListResponse getPastAuctionListResponse(Auction auction){
        var coverImage = assetService.getAssetByReferenceIdAndType(auction.getId(), AssetDomainType.AUCTION_COVER_IMAGE);
        return auctionMapper.convertToAuctionListResponse(auction, coverImage, 0, null);
    }

    private void setAuctionNo() {
        var numberedAuctions = getRepository().findAllByStateInOrderByStartTime(List.of(AuctionState.READY, AuctionState.STARTED, AuctionState.COMPLETED));
        IntStream.range(0, numberedAuctions.size()).forEach(index -> {
            var auction = numberedAuctions.get(index);
            auction.setAuctionNo(index + 1);
        });

        saveAll(numberedAuctions);
    }
}
