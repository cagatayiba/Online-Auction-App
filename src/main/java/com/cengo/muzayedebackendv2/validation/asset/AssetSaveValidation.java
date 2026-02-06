package com.cengo.muzayedebackendv2.validation.asset;

import com.cengo.muzayedebackendv2.domain.entity.enums.AssetDomainType;
import com.cengo.muzayedebackendv2.exception.message.ErrorMessage;
import com.cengo.muzayedebackendv2.repository.ArtistRepository;
import com.cengo.muzayedebackendv2.repository.LotRepository;
import com.cengo.muzayedebackendv2.repository.ProductRepository;
import com.cengo.muzayedebackendv2.validation.Validation;
import com.cengo.muzayedebackendv2.validation.ValidationContext;
import com.cengo.muzayedebackendv2.validation.ValidationStep;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class AssetSaveValidation implements Validation<AssetSaveValidation.Context> {
    private final ArtistRepository artistRepository;
    private final LotRepository lotRepository;
    private final ProductRepository productRepository;

    @Override
    public void validate(Context context) {
        initialize()
                .linkWith(new ArtistValidationStep(artistRepository))
                .linkWith(new LotStateValidationStep(lotRepository))
                .linkWith(new ProductStateValidationStep(productRepository))
                .linkWith(new ContextValidationStep())
                .validate(context);
    }

    @RequiredArgsConstructor
    private static class ArtistValidationStep extends ValidationStep<Context> {
        private final ArtistRepository artistRepository;

        @Override
        public void validate(Context context) {
            var artist = artistRepository.findById(context.referenceId);
            if (artist.isPresent()) context.setAssetDomainType(AssetDomainType.ARTIST_WORK);
            checkNext(context);
        }
    }

    @RequiredArgsConstructor
    private static class LotStateValidationStep extends ValidationStep<Context> {
        private final LotRepository lotRepository;

        @Override
        public void validate(Context context) {
            var lot = lotRepository.findById(context.referenceId);
            if (lot.isPresent()) {
                checkCondition(lot.get().getState().isUpdatable(), ErrorMessage.ASSET_NOT_SAVED);
                context.setAssetDomainType(AssetDomainType.LOT_MEDIA);
            }
            checkNext(context);
        }
    }

    @RequiredArgsConstructor
    private static class ProductStateValidationStep extends ValidationStep<Context> {
        private final ProductRepository productRepository;

        @Override
        public void validate(Context context) {
            var product = productRepository.findById(context.referenceId);
            if (product.isPresent()) {
                checkCondition(product.get().getState().isUpdatable(), ErrorMessage.ASSET_NOT_SAVED);
                context.setAssetDomainType(AssetDomainType.PRODUCT_MEDIA);
            }
            checkNext(context);
        }
    }

    private static class ContextValidationStep extends ValidationStep<Context> {
        @Override
        public void validate(Context context) {
            checkCondition(context.assetDomainType != null, ErrorMessage.ITEM_NOT_FOUND);
            checkNext(context);
        }
    }

    @AllArgsConstructor
    public static class Context implements ValidationContext {
        UUID referenceId;
        @Getter
        @Setter
        AssetDomainType assetDomainType;
    }
}
