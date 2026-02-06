package com.cengo.muzayedebackendv2.validation.watchlist;

import com.cengo.muzayedebackendv2.exception.message.ErrorMessage;
import com.cengo.muzayedebackendv2.repository.LotRepository;
import com.cengo.muzayedebackendv2.validation.Validation;
import com.cengo.muzayedebackendv2.validation.ValidationContext;
import com.cengo.muzayedebackendv2.validation.ValidationStep;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class WatchListSaveValidation implements Validation<WatchListSaveValidation.Context> {
    private final LotRepository lotRepository;

    @Override
    public void validate(Context context) {
        initialize()
                .linkWith(new LotValidationStep(lotRepository))
                .linkWith(new LotStateValidationStep(lotRepository))
                .validate(context);
    }

    @RequiredArgsConstructor
    private static class LotValidationStep extends ValidationStep<Context> {
        private final LotRepository lotRepository;

        @Override
        public void validate(Context context) {
            var lot = lotRepository.findById(context.lotId);
            checkCondition(lot.isPresent(), ErrorMessage.ITEM_NOT_FOUND);
            checkNext(context);
        }
    }

    @RequiredArgsConstructor
    private static class LotStateValidationStep extends ValidationStep<Context> {
        private final LotRepository lotRepository;

        @Override
        public void validate(Context context) {
            var lot = lotRepository.findById(context.lotId);
            checkCondition(lot.isPresent() && lot.get().getState().isStarted(), ErrorMessage.LOT_STATE_NOT_SUITABLE_TO_WATCHLIST);
            checkNext(context);
        }
    }

    @RequiredArgsConstructor
    public static class Context implements ValidationContext{
        final UUID lotId;
    }
}
