package com.cengo.muzayedebackendv2.service;

import com.cengo.muzayedebackendv2.config.properties.AppProperties;
import com.cengo.muzayedebackendv2.domain.dto.LotAuctionResponseDTO;
import com.cengo.muzayedebackendv2.domain.dto.LotResponseDTO;
import com.cengo.muzayedebackendv2.domain.dto.LotWatchlistResponseDTO;
import com.cengo.muzayedebackendv2.domain.dto.OfferWithBidsDTO;
import com.cengo.muzayedebackendv2.domain.dto.SaveAssetDTO;
import com.cengo.muzayedebackendv2.domain.entity.Auction;
import com.cengo.muzayedebackendv2.domain.entity.Lot;
import com.cengo.muzayedebackendv2.domain.entity.enums.AssetDomainType;
import com.cengo.muzayedebackendv2.domain.entity.enums.LotState;
import com.cengo.muzayedebackendv2.domain.request.LotSaveRequest;
import com.cengo.muzayedebackendv2.domain.request.LotUpdateRequest;
import com.cengo.muzayedebackendv2.domain.response.AssetResponse;
import com.cengo.muzayedebackendv2.domain.response.admin.LotAdminResponse;
import com.cengo.muzayedebackendv2.domain.response.admin.LotAdminSaleResponse;
import com.cengo.muzayedebackendv2.domain.response.admin.LotAdminTransferResponse;
import com.cengo.muzayedebackendv2.domain.response.bid.BidLotResponse;
import com.cengo.muzayedebackendv2.domain.response.lot.LotAuctionResponse;
import com.cengo.muzayedebackendv2.domain.response.lot.LotResponse;
import com.cengo.muzayedebackendv2.domain.response.lot.LotWatchlistResponse;
import com.cengo.muzayedebackendv2.domain.response.lot.LotWinningResponse;
import com.cengo.muzayedebackendv2.domain.response.user.UserBuyerResponse;
import com.cengo.muzayedebackendv2.domain.view.LotAuctionDetail;
import com.cengo.muzayedebackendv2.mapper.LotMapper;
import com.cengo.muzayedebackendv2.repository.LotAuctionDetailRepository;
import com.cengo.muzayedebackendv2.repository.LotRepository;
import com.cengo.muzayedebackendv2.security.UserPrincipal;
import com.cengo.muzayedebackendv2.service.base.BaseEntityService;
import com.cengo.muzayedebackendv2.util.rsql.RSQLSpecificationConverter;
import com.cengo.muzayedebackendv2.validation.lot.LotSaveValidation;
import com.cengo.muzayedebackendv2.validation.lot.LotUpdateValidation;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static com.cengo.muzayedebackendv2.repository.spec.LotAuctionDetailSpecs.auctionIdEquals;
import static com.cengo.muzayedebackendv2.repository.spec.LotAuctionDetailSpecs.userIdEquals;

@Service
public class LotService extends BaseEntityService<Lot, LotRepository> {

    private final ArtistService artistService;
    private final AssetService assetService;
    private final LotMapper lotMapper;
    private final LotSaveValidation lotSaveValidation;
    private final LotUpdateValidation lotIsUpdatableValidation;
    private final BidService bidService;
    private final OfferService offerService;
    private final AppProperties appProperties;
    private final WatchlistService watchlistService;
    private final AuctionService auctionService;
    private final UserService userService;
    private final LotAuctionDetailRepository lotAuctionDetailRepository;
    private final RSQLSpecificationConverter rsqlConverter;


    protected LotService(LotRepository repository, AssetService assetService, LotMapper lotMapper, @Lazy ArtistService artistService, LotSaveValidation lotSaveValidation, LotUpdateValidation lotIsUpdatableValidation, BidService bidService, @Lazy OfferService offerService, AppProperties appProperties, @Lazy WatchlistService watchlistService, @Lazy AuctionService auctionService, UserService userService, LotAuctionDetailRepository lotAuctionDetailRepository, RSQLSpecificationConverter rsqlConverter) {
        super(repository);
        this.artistService = artistService;
        this.assetService = assetService;
        this.lotMapper = lotMapper;
        this.lotSaveValidation = lotSaveValidation;
        this.lotIsUpdatableValidation = lotIsUpdatableValidation;
        this.bidService = bidService;
        this.offerService = offerService;
        this.appProperties = appProperties;
        this.watchlistService = watchlistService;
        this.auctionService = auctionService;
        this.userService = userService;
        this.lotAuctionDetailRepository = lotAuctionDetailRepository;
        this.rsqlConverter = rsqlConverter;
    }

    @Transactional
    public LotAdminResponse saveLot(LotSaveRequest request, MultipartFile coverImage, List<MultipartFile> images) {
        var lot = save(lotMapper.convertToLot(request));

        lotSaveValidation.validate(new LotSaveValidation.Context(lot));

        images = images == null ? Stream.of((MultipartFile) null).toList() : images;

        var coverImageAsset = saveLotCoverImage(coverImage, lot.getId());
        var mediaAssets = saveLotMediaImages(images, lot.getId());
        var artistName = artistService.getArtistById(lot.getArtistId()).fullName();

        return lotMapper.convertToLotAdminResponse(lot, coverImageAsset, mediaAssets, artistName);
    }

    @Transactional
    public LotAdminSaleResponse confirmSale(UUID lotId) {
        Lot lot = getById(lotId);

        //TODO validation check
        //lot state check

        lot.setState(LotState.APPROVED);

        return getLotSaleResponse(save(lot));
    }

    @Transactional
    public LotAdminSaleResponse transferOwner(UUID lotId) {
        Lot lot = getById(lotId);

        //TODO validation check
        // next offer yoksa zaten isteği kabul etmeyeceğiz ondan dolayı alttakı logic'i kaldırdım
        //lot state check

        var nextOfferDTO = offerService.getNextOffer(lot.getWinnerOfferId());
        var nextOffer = nextOfferDTO.offer();
        if (nextOffer.isEmpty()) throw new RuntimeException("move this logic to validation");

        var offer = nextOffer.get();
        lot.setWinnerOfferId(offer.getId());
        lot.setBuyerId(offer.getUserId());
        lot.setFinalPrice(nextOfferDTO.price());

        return getLotSaleResponse(save(lot));
    }

    public LotAdminSaleResponse cancelSale(UUID lotId) {
        Lot lot = getById(lotId);

        // TODO validation

        lot.setState(LotState.NOT_SOLD);
        lot.setWinnerOfferId(null);

        return getLotSaleResponse(save(lot));
    }

    public LotAdminTransferResponse getTransferInfo(UUID lotId) {
        Lot lot = getById(lotId);

        //TODO validation check
        //lot state check

        var nextOffer = offerService.getNextOfferInfo(lot.getWinnerOfferId());
        return new LotAdminTransferResponse(nextOffer.price());
    }

    @Transactional
    public LotAdminResponse updateLot(UUID lotId, LotUpdateRequest lotUpdateRequest) {
        Lot lot = getById(lotId);

        lotIsUpdatableValidation.validate(new LotUpdateValidation.Context(lot));

        lotMapper.updateLot(lot, lotUpdateRequest);

        return getLotAdminResponse(save(lot));
    }

    @Transactional
    public void deleteLotById(UUID lotId) {
        var lot = getById(lotId);

        lotIsUpdatableValidation.validate(new LotUpdateValidation.Context(lot));

        deleteLot(lot);
    }

    public LotAdminResponse adminGetLotById(UUID lotId) {
        return getLotAdminResponse(getById(lotId));
    }

    public Page<LotAdminResponse> adminGetLotsByAuctionId(UUID auctionId, Pageable pageable) {
        var lots = getRepository().findAllByAuctionIdOrderByLotNumber(auctionId, pageable);
        return lots.map(this::getLotAdminResponse);
    }

    public Page<LotAdminSaleResponse> adminGetSalesByAuctionId(UUID auctionId, Pageable pageable) {
        var lots = getRepository().findAllByAuctionIdOrderByLotNumber(auctionId, pageable);
        return lots.map(this::getLotSaleResponse);
    }

    public Lot getLotById(UUID lotId) {
        return getById(lotId);
    }

    public Lot saveLotEntity(Lot lot) {
        return save(lot);
    }

    public LotResponse getLotResponse(UUID lotId, UserPrincipal user) {
        Lot lot = getById(lotId);
        var bids = bidService.getAllBidsByLotId(lotId);

        Boolean isBidPlaced = null;
        Boolean isWinner = null;
        List<BidLotResponse> bidResponses = null;
        OfferWithBidsDTO userActiveOfferWithBids = null;
        if (user != null) {
            var userId = user.getUserId();
            var winnerUser = !bids.isEmpty() ? bids.getLast().getUserId() : null;

            bidResponses = bidService.getAllBidResponsesByLotId(bids, userId);
            isBidPlaced = bids.stream().anyMatch(bid -> bid.getUserId().equals(userId));
            isWinner = winnerUser != null && winnerUser.equals(userId);
            if (Boolean.TRUE.equals(isWinner))
                userActiveOfferWithBids = lot.getWinnerOfferId().map(offerService::getOfferWithBidsById).orElse(null);
        }

        var dto = LotResponseDTO.builder()
                .artistInfo(artistService.getArtistInfo(lot.getArtistId()))
                .coverImage(assetService.getAssetByReferenceIdAndType(lotId, AssetDomainType.LOT_COVER))
                .assets(assetService.getAssetsByReferenceIdAndType(lotId, AssetDomainType.LOT_MEDIA))
                .isBidPlaced(isBidPlaced)
                .isWinner(isWinner)
                .bidCount(bids.size())
                .userActiveOffer(userActiveOfferWithBids)
                .givenBids(bidResponses)
                .build();

        return lotMapper.convertToLotResponse(lot, dto);
    }

    public List<Lot> getLotEntitiesByAuctionId(UUID auctionId) {
        return getRepository().findAllByAuctionId(auctionId);
    }

    public Page<LotAuctionResponse> getLotsByAuctionIdV2(UUID auctionId, UserPrincipal user, String filter, Pageable pageable) {
        var userId = user != null ? user.getUserId() : appProperties.observerUserId();

        Specification<LotAuctionDetail> defaultSpec = auctionIdEquals(auctionId).and(userIdEquals(userId));
        Specification<LotAuctionDetail> rsqlSpec = rsqlConverter.convertToSpec(filter);
        var finalSpec = defaultSpec.and(rsqlSpec);

        var lotNumberSorting = Sort.sort(LotAuctionDetail.class).by(LotAuctionDetail::getLotNumber).ascending();
        var modifiedSort = pageable.getSort().and(lotNumberSorting);

        var modifiedPageRequest = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), modifiedSort);

        var details = lotAuctionDetailRepository.findAll(finalSpec, modifiedPageRequest);
        return details.map(this::getLotAuctionResponseV2);
    }

    public LotAuctionResponse getLotAuctionResponseV2(LotAuctionDetail detail) {
        var isCurrentAuction = detail.getAuctionState().isStarted();
        var isWinner = detail.getIsWinner();
        var lotState = detail.getState();
        var bannerText = lotState.getBannerText(isWinner, isCurrentAuction);

        return LotAuctionResponse.builder()
                .id(isCurrentAuction ? detail.getId().getLotId() : null)
                .lotNumber(detail.getLotNumber())
                .name(detail.getName())
                .description(detail.getDescription())
                .artistName(artistService.getArtistById(detail.getArtistId()).fullName())
                .coverImage(assetService.getAssetByReferenceIdAndType(detail.getId().getLotId(), AssetDomainType.LOT_COVER))
                .assets(assetService.getAssetsByReferenceIdAndType(detail.getId().getLotId(), AssetDomainType.LOT_MEDIA))
                .saleStartTime(detail.getAuctionStartTime())
                .saleEndTime(isCurrentAuction ? detail.getSaleEndTime() : null)
                .isBiddingEnabled(lotState.isStarted())
                .isCurrentAuction(isCurrentAuction)
                .isBidPlaced(detail.getIsBidPlaced())
                .isWinner(isWinner)
                .isInWatchList(detail.getIsInWatchlist())
                .bidCount(isCurrentAuction ? detail.getBidCount() : null)
                .currentPrice(isCurrentAuction ? detail.getCurrentPrice() : null)
                .bannerText(bannerText)
                .build();
    }

    public Page<LotAuctionResponse> getLotsByAuctionId(Auction auction, UserPrincipal user, Pageable pageable) {
        var lots = getRepository().findAllByAuctionIdOrderByLotNumber(auction.getId(), pageable);

        return lots.map(lot -> getLotAuctionResponse(lot, user, auction));
    }

    private LotAuctionResponse getLotAuctionResponse(Lot lot, UserPrincipal user, Auction auction) {
        var bids = bidService.getAllBidsByLotId(lot.getId());

        Boolean isWinner = null;
        Boolean isBidPlaced = null;
        String bannerText = null;
        Boolean isInWatchlist = null;
        if (user != null) {
            var userId = user.getUserId();
            var winnerUserId = !bids.isEmpty() ? bids.getLast().getUserId() : null;

            isBidPlaced = bids.stream().anyMatch(bid -> bid.getUserId().equals(userId));
            isWinner = winnerUserId != null && winnerUserId.equals(userId);
            bannerText = lot.getState().getBannerText(isWinner, auction.isCurrentAuction());
            isInWatchlist = watchlistService.existsByUserIdAndLotId(userId, lot.getId());
        }

        var dto = LotAuctionResponseDTO.builder()
                .artistName(artistService.getArtistById(lot.getArtistId()).fullName())
                .coverImage(assetService.getAssetByReferenceIdAndType(lot.getId(), AssetDomainType.LOT_COVER))
                .assets(assetService.getAssetsByReferenceIdAndType(lot.getId(), AssetDomainType.LOT_MEDIA))
                .isBiddingEnabled(lot.getState().isStarted())
                .isCurrentAuction(auction.isCurrentAuction())
                .isBidPlaced(isBidPlaced)
                .isWinner(isWinner)
                .isInWatchList(isInWatchlist)
                .bidCount(bids.size())
                .bannerText(bannerText)
                .saleStartTime(auction.getStartTime())
                .build();

        return lotMapper.convertToLotAuctionResponse(lot, dto, auction.isCurrentAuction());
    }

    public LotWatchlistResponse getLotWatchListResponse(UUID lotId, UUID userId) {
        var lot = getById(lotId);
        var bids = bidService.getAllBidsByLotId(lot.getId());
        var auction = auctionService.getAuctionById(lot.getAuctionId());

        // TODO iyileştirmeler yapılabilir
        var winnerUser = !bids.isEmpty() ? bids.getLast().getUserId() : null;

        var isCurrentAuction = auction.isCurrentAuction();
        var isBidPlaced = bids.stream().anyMatch(bid -> bid.getUserId().equals(userId));
        var isWinner = winnerUser != null && winnerUser.equals(userId);
        var bannerText = lot.getState().getBannerText(isWinner, isCurrentAuction);

        var dto = LotWatchlistResponseDTO.builder()
                .artistName(artistService.getArtistById(lot.getArtistId()).fullName())
                .coverImage(assetService.getAssetByReferenceIdAndType(lot.getId(), AssetDomainType.LOT_COVER))
                .assets(assetService.getAssetsByReferenceIdAndType(lot.getId(), AssetDomainType.LOT_MEDIA))
                .isBiddingEnabled(lot.getState().isStarted())
                .isCurrentAuction(isCurrentAuction)
                .isBidPlaced(isBidPlaced)
                .isWinner(isWinner)
                .bidCount(bids.size())
                .bannerText(bannerText)
                .build();

        return lotMapper.convertToLotWatchlistResponse(lot, dto, isCurrentAuction);
    }

    public List<LotWinningResponse> getUserWinningLots(UUID userId, UUID auctionId) {
        var lots = getRepository().findAllByBuyerIdAndAuctionId(userId, auctionId);
        return lots.stream().map(lot -> {
            var coverImageAsset = assetService.getAssetByReferenceIdAndType(lot.getId(), AssetDomainType.LOT_COVER);
            var mediaAssets = assetService.getAssetsByReferenceIdAndType(lot.getId(), AssetDomainType.LOT_MEDIA);
            var artistInfo = artistService.getArtistInfo(lot.getArtistId());
            return lotMapper.convertToLotWinningResponse(lot, coverImageAsset, mediaAssets, artistInfo);
        }).toList();
    }

    public Set<UUID> getAuctionsByUserId(UUID userId) {
        var lots = getRepository().findAllByBuyerId(userId);
        return lots.stream().map(Lot::getAuctionId).collect(Collectors.toSet());
    }

    @Transactional
    public void deleteAllByAuctionId(UUID auctionId) {
        List<Lot> lots = findLotsByAuctionId(auctionId);
        lots.forEach(this::deleteLot);
    }

    @Transactional
    public void deleteAllLots(List<Lot> lots) {
        lots.forEach(this::deleteLot);
    }

    public void deleteLot(Lot lot) {
        assetService.deleteAllByReferenceId(lot.getId());
        delete(lot);
    }

    public void startLots(UUID auctionId) {
        List<Lot> lots = findLotsByAuctionId(auctionId);
        lots.forEach(lot -> lot.setState(LotState.ON_SALE));
        saveAll(lots);
    }

    public List<Lot> endLots(UUID auctionId) {
        List<Lot> lots = findLotsByAuctionId(auctionId);
        Map<Boolean, List<Lot>> partition = lots.stream()
                .collect(Collectors.partitioningBy(Lot::isEnd));

        var endLots = partition.get(true);
        var currentLots = partition.get(false);

        endLots.forEach(lot -> {
            if (lot.getWinnerOfferId().isPresent()) {
                lot.setState(LotState.SOLD);
                lot.setBuyerId(offerService.getUserId(lot.getWinnerOfferId().get()));
                //TODO async promotion
            } else {
                lot.setState(LotState.NOT_SOLD);
            }
        });

        return Stream.concat(currentLots.stream(), saveAll(endLots).stream()).toList();
    }

    public List<Lot> findLotsByAuctionId(UUID auctionId) {
        return getRepository().findAllByAuctionId(auctionId);
    }

    public List<Lot> findLotsByArtistId(UUID artistId) {
        return getRepository().findAllByArtistId(artistId);
    }

    public void setLotsReady(List<Lot> lots, LocalDateTime endTime) {
        IntStream.range(0, lots.size()).forEach(index -> {
            var lot = lots.get(index);
            lot.setLotNumber(index + 1);
            lot.setEndTime(endTime.plusMinutes(index * appProperties.lotEndTimeInterval()));
            lot.setState(LotState.READY);
        });
        saveAll(lots);
    }

    public void setLotsDraft(UUID auctionId) {
        var lots = findLotsByAuctionId(auctionId);
        lots.forEach(lot -> lot.setState(LotState.DRAFT));

        saveAll(lots);
    }


    private LotAdminSaleResponse getLotSaleResponse(Lot lot) {
        var lotAdminResponse = getLotAdminResponse(lot);

        // TODO burada optionallarla falan bu kadar dikkatli olmamızı gerektirecek ne var anlamadım zaten satış olmuş winnerUser falan hep olmalı gibi gibi
        var winnerUserId = lot.getWinnerOfferId()
                .map(offerService::getUserId);
        var buyer = winnerUserId.map(userService::getBuyerUserById).orElse(null);

        var isTransferEnabled = winnerUserId.map(buyerId ->
                bidService.isNextBidExists(lot.getId(), buyerId, lot.getCurrentPrice())
        ).orElse(null);

        return lotMapper.convertToLotAdminSaleResponse(lotAdminResponse, buyer, isTransferEnabled);
    }

    private LotAdminResponse getLotAdminResponse(Lot lot) {
        var coverImageAsset = assetService.getAssetByReferenceIdAndType(lot.getId(), AssetDomainType.LOT_COVER);
        var mediaAssets = assetService.getAssetsByReferenceIdAndType(lot.getId(), AssetDomainType.LOT_MEDIA);
        var artistName = artistService.getArtistById(lot.getArtistId()).fullName();

        return lotMapper.convertToLotAdminResponse(lot, coverImageAsset, mediaAssets, artistName);
    }

    private AssetResponse saveLotCoverImage(MultipartFile coverImage, UUID lotId) {
        return assetService.saveAsset(
                SaveAssetDTO.builder()
                        .referenceId(lotId)
                        .file(coverImage)
                        .domainType(AssetDomainType.LOT_COVER)
                        .build()
        );
    }

    private List<AssetResponse> saveLotMediaImages(List<MultipartFile> images, UUID lotId) {
        List<SaveAssetDTO> assetsToSave = IntStream.range(0, images.size())
                .mapToObj(index ->
                        SaveAssetDTO.builder()
                                .referenceId(lotId)
                                .file(images.get(index))
                                .domainType(AssetDomainType.LOT_MEDIA)
                                .orderNo(index + 1)
                                .build()

                ).toList();

        return assetService.saveAssets(assetsToSave);
    }

    private UserBuyerResponse getBuyer(Lot lot) {
        var winnerOfferId = lot.getWinnerOfferId();
        if (winnerOfferId.isPresent()) {
            var winnerUserId = lot.getWinnerOfferId().map(offerService::getUserId);
            return winnerUserId.map(userService::getBuyerUserById).orElse(null);
        }
        return null;
    }
}
