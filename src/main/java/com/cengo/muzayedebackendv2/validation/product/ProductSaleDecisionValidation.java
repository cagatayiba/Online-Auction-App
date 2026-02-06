package com.cengo.muzayedebackendv2.validation.product;

import com.cengo.muzayedebackendv2.domain.entity.Product;
import com.cengo.muzayedebackendv2.exception.message.ErrorMessage;
import com.cengo.muzayedebackendv2.repository.UserRepository;
import com.cengo.muzayedebackendv2.validation.Validation;
import com.cengo.muzayedebackendv2.validation.ValidationContext;
import com.cengo.muzayedebackendv2.validation.ValidationStep;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProductSaleDecisionValidation implements Validation<ProductSaleDecisionValidation.Context> {
    private final UserRepository userRepository;

    @Override
    public void validate(Context context) {
        initialize()
                .linkWith(new ProductStateValidationStep())
                .linkWith(new RequesterValidationStep(userRepository))
                .validate(context);
    }

    @RequiredArgsConstructor
    private static class RequesterValidationStep extends ValidationStep<Context> {
        private final UserRepository userRepository;

        @Override
        public void validate(Context context) {
            var user = userRepository.findById(context.product.getRequesterId());
            checkCondition(user.isPresent(), ErrorMessage.ITEM_NOT_FOUND);
            checkNext(context);
        }
    }

    private static class ProductStateValidationStep extends ValidationStep<Context> {
        @Override
        public void validate(Context context) {
            checkCondition(context.product.getState().isRequested(), ErrorMessage.PRODUCT_NOT_BOUGHT);
            checkNext(context);
        }
    }

    @AllArgsConstructor
    public static class Context implements ValidationContext {
        Product product;
    }
}
