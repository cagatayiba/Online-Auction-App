package com.cengo.muzayedebackendv2.service;

import com.cengo.muzayedebackendv2.config.offer.OfferInterval;
import com.cengo.muzayedebackendv2.config.properties.AppProperties;
import com.cengo.muzayedebackendv2.domain.dto.*;
import com.cengo.muzayedebackendv2.domain.entity.Lot;
import com.cengo.muzayedebackendv2.domain.entity.Offer;
import com.cengo.muzayedebackendv2.domain.enums.NotificationType;
import com.cengo.muzayedebackendv2.domain.enums.PointBenefitAction;
import com.cengo.muzayedebackendv2.domain.message.NewOfferGivenMessage;
import com.cengo.muzayedebackendv2.domain.request.OfferRequest;
import com.cengo.muzayedebackendv2.domain.response.SaveOfferResponse;
import com.cengo.muzayedebackendv2.domain.response.PriceOptionsResponse;
import com.cengo.muzayedebackendv2.exception.BidConflictException;
import com.cengo.muzayedebackendv2.mapper.BidMapper;
import com.cengo.muzayedebackendv2.mapper.OfferMapper;
import com.cengo.muzayedebackendv2.repository.OfferRepository;
import com.cengo.muzayedebackendv2.service.auctioneer.AuctioneerService;
import com.cengo.muzayedebackendv2.service.base.BaseEntityService;
import com.cengo.muzayedebackendv2.service.mail.MailTemplateService;
import com.cengo.muzayedebackendv2.util.LotTimeManager;
import com.cengo.muzayedebackendv2.validation.offer.OfferSaveValidation;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
public class OfferService extends BaseEntityService<Offer, OfferRepository> {
    private static final String SUCCESSFUL_BID_MESSAGE = "Teklifinizi başarıyla verdiniz. Güncel kazanan sizsiniz";
    private static final String UNSUCCESSFUL_BID_MESSAGE = "En yüksek teklifin altında kaldınız. Lütfen daha yüksek teklif vermeyi deneyiniz";
    private static final String SOCKET_TOPIC = "/bids";

    private final OfferMapper offerMapper;
    private final LotService lotService;
    private final BidService bidService;

    private final OfferSaveValidation offerSaveValidation;
    private final OfferInterval offerInterval;
    private final AuctioneerService auctioneerService;
    private final BidMapper bidMapper;
    private final LotTimeManager lotTimeManager;
    private final SimpMessageSendingOperations socket;
    private final AppProperties appProperties;
    private final NotificationService notificationService;
    private final MailTemplateService mailTemplateService;
    private final UserService userService;
    private final PointBenefitService pointBenefitService;

    protected OfferService(OfferRepository repository, OfferMapper offerMapper, OfferSaveValidation offerSaveValidation, LotService lotService, BidService bidService, OfferInterval offerInterval, AuctioneerService auctioneerService, BidMapper bidMapper, LotTimeManager lotTimeManager, SimpMessageSendingOperations socket, AppProperties appProperties, NotificationService notificationService, MailTemplateService mailTemplateService, UserService userService, PointBenefitService pointBenefitService) {
        super(repository);
        this.offerMapper = offerMapper;
        this.offerSaveValidation = offerSaveValidation;
        this.lotService = lotService;
        this.bidService = bidService;
        this.offerInterval = offerInterval;
        this.auctioneerService = auctioneerService;
        this.bidMapper = bidMapper;
        this.lotTimeManager = lotTimeManager;
        this.socket = socket;
        this.appProperties = appProperties;
        this.notificationService = notificationService;
        this.mailTemplateService = mailTemplateService;
        this.userService = userService;
        this.pointBenefitService = pointBenefitService;
    }

    @Retryable(retryFor = BidConflictException.class)
    @Transactional
    public SaveOfferResponse saveOffer(OfferRequest offerRequest, UUID userId) {
        var offer = offerMapper.convertToOffer(offerRequest, userId);
        var lot = lotService.getLotById(offer.getLotId());

        offerSaveValidation.validate(new OfferSaveValidation.Context(offer, lot));

        var currentOfferId = lot.getWinnerOfferId();
        var currentOffer = currentOfferId.flatMap(offerId -> getRepository().findById(offerId));

        if (currentOffer.isPresent() && Boolean.TRUE.equals(currentOffer.get().isOfferFromSameUser(userId))){
            updateOfferWithNewPrice(currentOffer.get(), offer.getPrice());
            return getSuccessfulOfferResponse();
        }
        offer = save(offer);

        var offerResult = auctioneerService.getResult(offer, currentOffer, lot.getCurrentPrice());

        var lotId = lot.getId();
        var newBids = offerResult.givenBids().stream()
                .map( bidDTO -> bidMapper.convertToBid(bidDTO, lotId) )
                .toList();
        bidService.saveAllBids(newBids);

        lot.setFinalPrice(offerResult.updatedPrice());
        lot.setWinnerOfferId(offerResult.winnerOffer().getId());
        var extraTime = extendLotEndTimeIfNecessary(lot, offer);
        try {
            lot = lotService.saveLotEntity(lot);
        }catch (OptimisticLockingFailureException e){
            throw new BidConflictException();
        }
        // TODO retryable ve transactional birlikte çalışabilityor mu

        if (currentOffer.isPresent() && Boolean.TRUE.equals(offerResult.isBuyerReplaced())){
            var currentUserId = currentOffer.get().getUserId();
            var notificationDto = SendNotificationDTO.builder()
                    .userId(currentUserId)
                    .type(NotificationType.BID)
                    .productName("Lot "+ lot.getLotNumber())
                    .link(NotificationType.BID.getLink(lot.getId()))
                    .build();
            notificationService.saveNotification(notificationDto);

            var currentUser = userService.getUserById(currentUserId);
            var mailDto = SendBidInfoDTO.builder()
                    .lotId(lot.getId())
                    .lotName(String.valueOf(lot.getLotNumber()))
                    .userName(currentUser.getName())
                    .price(offerResult.updatedPrice())
                    .email(currentUser.getEmail())
                    .build();
            mailTemplateService.sendBidInfoMail(mailDto);
        }

        SaveOfferResponse response;
        if(Boolean.TRUE.equals(offerResult.hasNewOfferWon())){
            response = getSuccessfulOfferResponse();
            pointBenefitService.notifyAction(userId, PointBenefitAction.SUCCESSFUL_OFFER_GIVEN);
        }else {
            response = getFailedOfferResponse();
        }

        // make sure this is always the last step
        socket.convertAndSend(SOCKET_TOPIC, prepareNewOfferGivenMessage(lot, offerResult.winnerOffer(), extraTime, bidService.getLotBidCount(lot.getId())));

        return response;
    }

    public PriceOptionsResponse getPriceOptions(UUID lotId, UUID userId) {
        var lot = lotService.getLotById(lotId);
        var winnerOfferOpt = lot.getWinnerOfferId().map(this::getById);

        var startingPrice = winnerOfferOpt
                .filter(winnerOffer -> winnerOffer.getUserId().equals(userId))
                .map(Offer::getPrice)
                .orElse(lot.getCurrentPrice());

        // if no offer given yet, include current price by sending 3rd param true
        var prices = offerInterval.getOfferPrices(startingPrice, appProperties.offerLimit(),  winnerOfferOpt.isPresent());

        return new PriceOptionsResponse(prices);
    }

    public UUID getUserId(UUID winnerOfferId) {
        return getById(winnerOfferId).getUserId();
    }

    public OfferWithBidsDTO getOfferWithBidsById(UUID offerId){
        var offer = getById(offerId);
        var associatedBids = bidService.getAllBidsByOfferId(offerId);

        return OfferWithBidsDTO.builder()
                .offer(offer)
                .associatedBids(associatedBids)
                .build();
    }

    public NextOfferDTO getNextOffer(Optional<UUID> winnerOfferId) {
        if (winnerOfferId.isPresent()) {
            Offer offer = getById(winnerOfferId.get());
            var nextBid = bidService.getNextBid(offer.getLotId(), offer.getUserId());
            if (nextBid != null) {
                var nextOffer = getById(nextBid.getOfferId());
                return new NextOfferDTO(Optional.of(nextOffer), nextBid.getPrice());
            }
        }
        return new NextOfferDTO(Optional.empty(), null);
    }

    public NextOfferDTO getNextOfferInfo(Optional<UUID> winnerOfferId) {
        if (winnerOfferId.isPresent()) {
            Offer offer = getById(winnerOfferId.get());
            var nextBid = bidService.getNextBidInfo(offer.getLotId(), offer.getUserId());
            if (nextBid != null) {
                var nextOffer = getById(nextBid.getOfferId());
                return new NextOfferDTO(Optional.of(nextOffer), nextBid.getPrice());
            }
        }
        return new NextOfferDTO(Optional.empty(), null);
    }


    private NewOfferGivenMessage prepareNewOfferGivenMessage(Lot lot, Offer offer, Integer extraTime, Integer bidCount){
        return NewOfferGivenMessage.builder()
                .lotId(lot.getId())
                .lotPrice(lot.getCurrentPrice())
                .userId(offer.getUserId())
                .extraTime(extraTime)
                .bidCount(bidCount)
                .build();
    }

    private int extendLotEndTimeIfNecessary(Lot lot, Offer offer){
        var extraTime = lotTimeManager.getAuctionExtraTime(
                lot.getEndTime(),
                lot.getExtendedEndTime(),
                offer.getTime()
        );

        if(extraTime > 0) lot.setExtendedEndTime(lot.getSaleEndTime().plusMinutes(extraTime));

        return extraTime;
    }

    private void updateOfferWithNewPrice(Offer offer, Integer newPrice) {
        offer.setPrice(newPrice);
        save(offer);
    }

    private SaveOfferResponse getSuccessfulOfferResponse(){
        return new SaveOfferResponse(true, SUCCESSFUL_BID_MESSAGE);
    }

    private SaveOfferResponse getFailedOfferResponse(){
        return new SaveOfferResponse(false, UNSUCCESSFUL_BID_MESSAGE);
    }
}
