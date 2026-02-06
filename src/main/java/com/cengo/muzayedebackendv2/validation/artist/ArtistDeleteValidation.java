package com.cengo.muzayedebackendv2.validation.artist;

import com.cengo.muzayedebackendv2.domain.entity.Lot;
import com.cengo.muzayedebackendv2.domain.entity.Product;
import com.cengo.muzayedebackendv2.domain.entity.enums.LotState;
import com.cengo.muzayedebackendv2.domain.entity.enums.ProductState;
import com.cengo.muzayedebackendv2.exception.message.ErrorMessage;
import com.cengo.muzayedebackendv2.validation.Validation;
import com.cengo.muzayedebackendv2.validation.ValidationContext;
import com.cengo.muzayedebackendv2.validation.ValidationStep;
import lombok.AllArgsConstructor;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ArtistDeleteValidation implements Validation<ArtistDeleteValidation.Context> {

    @Override
    public void validate(Context context) {
        initialize()
                .linkWith(new LotStateValidationStep())
                .linkWith(new ProductStateValidationStep())
                .validate(context);
    }

    private static class LotStateValidationStep extends ValidationStep<Context> {
        @Override
        public void validate(Context context) {
            var lots = context.lots;
            checkCondition(lots.isEmpty() || lots.stream().map(Lot::getState).allMatch(LotState::isUpdatable), ErrorMessage.ARTIST_NOT_DELETED);
            checkNext(context);
        }
    }

    private static class ProductStateValidationStep extends ValidationStep<Context> {
        @Override
        public void validate(Context context) {
            var products = context.products;
            checkCondition(products.isEmpty() || products.stream().map(Product::getState).allMatch(ProductState::isUpdatable), ErrorMessage.ARTIST_NOT_DELETED);
            checkNext(context);
        }
    }

    @AllArgsConstructor
    public static class Context implements ValidationContext {
        List<Lot> lots;
        List<Product> products;
    }
}
