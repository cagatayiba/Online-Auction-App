package com.cengo.muzayedebackendv2.validation.lot;

import com.cengo.muzayedebackendv2.domain.entity.Lot;
import com.cengo.muzayedebackendv2.exception.message.ErrorMessage;
import com.cengo.muzayedebackendv2.validation.Validation;
import com.cengo.muzayedebackendv2.validation.ValidationContext;
import com.cengo.muzayedebackendv2.validation.ValidationStep;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
public class LotUpdateValidation implements Validation<LotUpdateValidation.Context> {

    @Override
    public void validate(Context context) {
        initialize()
                .linkWith(new LotStateValidationStep())
                .validate(context);
    }

    private static class LotStateValidationStep extends ValidationStep<Context> {
        @Override
        public void validate(Context context) {
            checkCondition(context.lot.getState().isUpdatable(), ErrorMessage.LOT_NOT_UPDATED);
            checkNext(context);
        }
    }

    @AllArgsConstructor
    public static class Context implements ValidationContext {
        Lot lot;
    }
}
