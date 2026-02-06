package com.cengo.muzayedebackendv2.validation.auction;

import com.cengo.muzayedebackendv2.domain.entity.Auction;
import com.cengo.muzayedebackendv2.domain.entity.Lot;
import com.cengo.muzayedebackendv2.exception.message.ErrorMessage;
import com.cengo.muzayedebackendv2.validation.Validation;
import com.cengo.muzayedebackendv2.validation.ValidationContext;
import com.cengo.muzayedebackendv2.validation.ValidationStep;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AuctionSetReadyValidation implements Validation<AuctionSetReadyValidation.Context> {
    @Override
    public void validate(Context context) {
        initialize()
                .linkWith(new AuctionStateValidationStep())
                .linkWith(new LotsNotEmptyValidationStep())
                .validate(context);
    }

    private static class AuctionStateValidationStep extends ValidationStep<Context> {
        @Override
        public void validate(Context context) {
            checkCondition(context.auction.getState().isUpdatable(), ErrorMessage.AUCTION_NOT_UPDATED);
            checkNext(context);
        }
    }

    private static class LotsNotEmptyValidationStep extends ValidationStep<Context> {
        @Override
        public void validate(Context context) {
            checkCondition(!context.lots.isEmpty(), ErrorMessage.AUCTION_LOT_EMPTY);
            checkNext(context);
        }
    }

    @AllArgsConstructor
    public static class Context implements ValidationContext {
        Auction auction;
        List<Lot> lots;
    }
}
