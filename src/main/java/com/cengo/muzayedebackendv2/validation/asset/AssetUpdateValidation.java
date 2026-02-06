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
public class AssetUpdateValidation implements Validation<AssetUpdateValidation.Context> {
    private final AuctionRepository auctionRepository;
    private final LotRepository lotRepository;
    private final ProductRepository productRepository;

    @Override
    public void validate(Context context) {
        initialize()
                .linkWith(new AuctionStateValidationStep(auctionRepository))
                .linkWith(new LotStateValidationStep(lotRepository))
                .linkWith(new ProductStateValidationStep(productRepository))
                .validate(context);
    }

    @RequiredArgsConstructor
    private static class AuctionStateValidationStep extends ValidationStep<Context> {
        private final AuctionRepository auctionRepository;

        @Override
        public void validate(Context context) {
            var auction = auctionRepository.findById(context.asset.getReferenceId());
            checkCondition(auction.isEmpty() || auction.get().getState().isUpdatable(), ErrorMessage.ASSET_NOT_UPDATED);
            checkNext(context);
        }
    }

    @RequiredArgsConstructor
    private static class LotStateValidationStep extends ValidationStep<Context> {
        private final LotRepository lotRepository;

        @Override
        public void validate(Context context) {
            var lot = lotRepository.findById(context.asset.getReferenceId());
            checkCondition(lot.isEmpty() || lot.get().getState().isUpdatable(), ErrorMessage.ASSET_NOT_UPDATED);
            checkNext(context);
        }
    }

    @RequiredArgsConstructor
    private static class ProductStateValidationStep extends ValidationStep<Context> {
        private final ProductRepository productRepository;

        @Override
        public void validate(Context context) {
            var product = productRepository.findById(context.asset.getReferenceId());
            checkCondition(product.isEmpty() || product.get().getState().isUpdatable(), ErrorMessage.ASSET_NOT_UPDATED);
            checkNext(context);
        }
    }

    @AllArgsConstructor
    public static class Context implements ValidationContext {
        Asset asset;
    }
}
