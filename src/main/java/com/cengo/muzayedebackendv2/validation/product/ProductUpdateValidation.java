package com.cengo.muzayedebackendv2.validation.product;

import com.cengo.muzayedebackendv2.domain.entity.Product;
import com.cengo.muzayedebackendv2.exception.message.ErrorMessage;
import com.cengo.muzayedebackendv2.validation.Validation;
import com.cengo.muzayedebackendv2.validation.ValidationContext;
import com.cengo.muzayedebackendv2.validation.ValidationStep;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
public class ProductUpdateValidation implements Validation<ProductUpdateValidation.Context> {

    @Override
    public void validate(Context context) {
        initialize()
                .linkWith(new ProductStateValidationStep())
                .validate(context);
    }

    private static class ProductStateValidationStep extends ValidationStep<Context> {
        @Override
        public void validate(Context context) {
            checkCondition(context.product.getState().isUpdatable(), ErrorMessage.PRODUCT_NOT_UPDATED);
            checkNext(context);
        }
    }

    @AllArgsConstructor
    public static class Context implements ValidationContext {
        Product product;
    }
}
