package com.cengo.muzayedebackendv2.validation.lot;

import com.cengo.muzayedebackendv2.domain.entity.Lot;
import com.cengo.muzayedebackendv2.exception.message.ErrorMessage;
import com.cengo.muzayedebackendv2.repository.ArtistRepository;
import com.cengo.muzayedebackendv2.repository.AuctionRepository;
import com.cengo.muzayedebackendv2.validation.Validation;
import com.cengo.muzayedebackendv2.validation.ValidationContext;
import com.cengo.muzayedebackendv2.validation.ValidationStep;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LotSaveValidation implements Validation<LotSaveValidation.Context> {
    private final AuctionRepository auctionRepository;
    private final ArtistRepository artistRepository;

    @Override
    public void validate(Context context) {
        initialize()
                .linkWith(new ArtistValidationStep(artistRepository))
                .linkWith(new AuctionValidationStep(auctionRepository))
                .linkWith(new AuctionStateValidationStep(auctionRepository))
                .validate(context);
    }

    @RequiredArgsConstructor
    private static class ArtistValidationStep extends ValidationStep<Context> {
        private final ArtistRepository artistRepository;

        @Override
        public void validate(Context context) {
            var artist = artistRepository.findById(context.lot.getArtistId());
            checkCondition(artist.isPresent(), ErrorMessage.ITEM_NOT_FOUND);
            checkNext(context);
        }
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
            checkCondition(auction.isPresent() && auction.get().getState().isUpdatable(), ErrorMessage.AUCTION_NOT_UPDATED);
            checkNext(context);
        }
    }

    @AllArgsConstructor
    public static class Context implements ValidationContext {
        Lot lot;
    }
}
