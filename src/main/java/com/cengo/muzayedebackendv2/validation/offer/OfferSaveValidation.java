package com.cengo.muzayedebackendv2.validation.offer;

import com.cengo.muzayedebackendv2.config.offer.OfferInterval;
import com.cengo.muzayedebackendv2.config.properties.AppProperties;
import com.cengo.muzayedebackendv2.domain.entity.Lot;
import com.cengo.muzayedebackendv2.domain.entity.Offer;
import com.cengo.muzayedebackendv2.exception.message.BidErrorMessage;
import com.cengo.muzayedebackendv2.exception.message.ErrorMessage;
import com.cengo.muzayedebackendv2.repository.AuctionRepository;
import com.cengo.muzayedebackendv2.validation.Validation;
import com.cengo.muzayedebackendv2.validation.ValidationContext;
import com.cengo.muzayedebackendv2.validation.ValidationStep;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OfferSaveValidation implements Validation<OfferSaveValidation.Context> {

    private final AuctionRepository auctionRepository;
    private final OfferInterval offerInterval;
    private final AppProperties appProperties;

    @Override
    public void validate(Context context) {
        initialize()
                .linkWith(new AuctionValidationStep(auctionRepository))
                .linkWith(new AuctionStateValidationStep(auctionRepository))
                .linkWith(new LotStateValidationStep())
                .linkWith(new TimeValidationStep())
                .linkWith(new PriceValidationStep())
                .linkWith(new PriceIntervalValidationStep(offerInterval, appProperties))
                .validate(context);
    }

    @RequiredArgsConstructor
    private static class AuctionValidationStep extends ValidationStep<Context> {
        private final AuctionRepository auctionRepository;

        @Override
        public void validate(Context context) {
            var auction = auctionRepository.findById(context.lot.getAuctionId());
            checkCondition(auction.isPresent(), ErrorMessage.ITEM_NOT_FOUND);
            checkNext(context);
        }
    }

    @RequiredArgsConstructor
    private static class AuctionStateValidationStep extends ValidationStep<Context> {
        private final AuctionRepository auctionRepository;

        @Override
        public void validate(Context context) {
            var auction = auctionRepository.findById(context.lot.getAuctionId());
            checkCondition(auction.isPresent() && auction.get().getState().isStarted(), BidErrorMessage.AUCTION_NOT_STARTED);
            checkNext(context);
        }
    }

    private static class LotStateValidationStep extends ValidationStep<Context> {
        @Override
        public void validate(Context context) {
            checkCondition(context.lot.getState().isStarted(), BidErrorMessage.LOT_STATE_NOT_SUITABLE_FOR_OFFER);
            checkNext(context);
        }
    }

    private static class TimeValidationStep extends ValidationStep<Context> {
        @Override
        public void validate(Context context) {
            checkCondition(context.offer.getTime().isBefore(context.lot.getSaleEndTime()), BidErrorMessage.LOT_SALE_TIME_ENDED);
            checkNext(context);
        }
    }

    private static class PriceValidationStep extends ValidationStep<Context> {
        @Override
        public void validate(Context context) {
            var offer = context.offer;
            var lot = context.lot;
            var isFirstOffer = lot.getWinnerOfferId().isEmpty();
            checkCondition(
                    (isFirstOffer && offer.getPrice().equals(lot.getCurrentPrice())) || offer.getPrice() > lot.getCurrentPrice(),
                    BidErrorMessage.OFFER_NOT_GRATER_THAN_LOT_PRICE
            );
            checkNext(context);
        }
    }

    @RequiredArgsConstructor
    private static class PriceIntervalValidationStep extends ValidationStep<Context> {
        private final OfferInterval offerInterval;
        private final AppProperties appProperties;
        @Override
        public void validate(Context context) {
            checkCondition(offerInterval.isOfferInLimit(context.offer.getPrice(), context.lot.getCurrentPrice(), appProperties.offerLimit()),
                    BidErrorMessage.INVALID_OFFER_PRICE);
            checkNext(context);
        }
    }

    @AllArgsConstructor
    public static class Context implements ValidationContext {
        Offer offer;
        Lot lot;
    }
}
