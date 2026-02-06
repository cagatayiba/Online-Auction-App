package com.cengo.muzayedebackendv2.validation.product;

import com.cengo.muzayedebackendv2.domain.entity.Product;
import com.cengo.muzayedebackendv2.exception.message.ErrorMessage;
import com.cengo.muzayedebackendv2.repository.ArtistRepository;
import com.cengo.muzayedebackendv2.validation.Validation;
import com.cengo.muzayedebackendv2.validation.ValidationContext;
import com.cengo.muzayedebackendv2.validation.ValidationStep;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProductSaveValidation implements Validation<ProductSaveValidation.Context> {
    private final ArtistRepository artistRepository;

    @Override
    public void validate(Context context) {
        initialize()
                .linkWith(new ArtistValidationStep(artistRepository))
                .validate(context);
    }

    @RequiredArgsConstructor
    private static class ArtistValidationStep extends ValidationStep<Context> {
        private final ArtistRepository artistRepository;

        @Override
        public void validate(Context context) {
            var artist = artistRepository.findById(context.product.getArtistId());
            checkCondition(artist.isPresent(), ErrorMessage.ITEM_NOT_FOUND);
            checkNext(context);
        }
    }

    @AllArgsConstructor
    public static class Context implements ValidationContext {
        Product product;
    }
}
