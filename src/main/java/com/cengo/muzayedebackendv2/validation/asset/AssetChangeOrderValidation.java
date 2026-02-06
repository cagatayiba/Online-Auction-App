package com.cengo.muzayedebackendv2.validation.asset;

import com.cengo.muzayedebackendv2.domain.entity.Asset;
import com.cengo.muzayedebackendv2.exception.message.ErrorMessage;
import com.cengo.muzayedebackendv2.repository.AuctionRepository;
import com.cengo.muzayedebackendv2.repository.LotRepository;
import com.cengo.muzayedebackendv2.repository.ProductRepository;
import com.cengo.muzayedebackendv2.validation.Validation;
import com.cengo.muzayedebackendv2.validation.ValidationContext;
import com.cengo.muzayedebackendv2.validation.ValidationStep;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AssetChangeOrderValidation implements Validation<AssetChangeOrderValidation.Context> {
    private final AuctionRepository auctionRepository;
    private final LotRepository lotRepository;
    private final ProductRepository productRepository;

    @Override
    public void validate(Context context) {
        initialize()
                .linkWith(new AssetIdValidationStep())
                .linkWith(new AssetReferenceIdValidationStep())
                .linkWith(new AuctionStateValidationStep(auctionRepository))
                .linkWith(new LotStateValidationStep(lotRepository))
                .linkWith(new ProductStateValidationStep(productRepository))
                .validate(context);
    }


    private static class AssetIdValidationStep extends ValidationStep<Context> {
        @Override
        public void validate(Context context) {
            checkCondition(!context.asset1.getId().equals(context.asset2.getId()), ErrorMessage.ASSET_ORDER_SAME_ID);
            checkNext(context);
        }
    }

    private static class AssetReferenceIdValidationStep extends ValidationStep<Context> {
        @Override
        public void validate(Context context) {
            checkCondition(context.asset1.getReferenceId().equals(context.asset2.getReferenceId())
                            && context.asset1.getDomainType().equals(context.asset2.getDomainType()),
                    ErrorMessage.ASSET_ORDER_NOT_CHANGED);
            checkNext(context);
        }
    }

    @RequiredArgsConstructor
    private static class AuctionStateValidationStep extends ValidationStep<Context> {
        private final AuctionRepository auctionRepository;

        @Override
        public void validate(Context context) {
            var auction = auctionRepository.findById(context.asset1.getReferenceId());
            checkCondition(auction.isEmpty() || auction.get().getState().isUpdatable(), ErrorMessage.ASSET_NOT_UPDATED);
            checkNext(context);
        }
    }

    @RequiredArgsConstructor
    private static class LotStateValidationStep extends ValidationStep<Context> {
        private final LotRepository lotRepository;

        @Override
        public void validate(Context context) {
            var lot = lotRepository.findById(context.asset1.getReferenceId());
            checkCondition(lot.isEmpty() || lot.get().getState().isUpdatable(), ErrorMessage.ASSET_NOT_UPDATED);
            checkNext(context);
        }
    }

    @RequiredArgsConstructor
    private static class ProductStateValidationStep extends ValidationStep<Context> {
        private final ProductRepository productRepository;

        @Override
        public void validate(Context context) {
            var product = productRepository.findById(context.asset1.getReferenceId());
            checkCondition(product.isEmpty() || product.get().getState().isUpdatable(), ErrorMessage.ASSET_NOT_UPDATED);
            checkNext(context);
        }
    }


    @AllArgsConstructor
    public static class Context implements ValidationContext {
        Asset asset1;
        Asset asset2;
    }
}
