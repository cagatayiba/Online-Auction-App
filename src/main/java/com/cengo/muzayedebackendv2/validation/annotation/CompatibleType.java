package com.cengo.muzayedebackendv2.validation.annotation;

import com.cengo.muzayedebackendv2.domain.entity.Asset;
import com.cengo.muzayedebackendv2.domain.entity.enums.AssetMimeType;
import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = CompatibleType.Validator.class)
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface CompatibleType {
    String message() default "Desteklenmeyen dosya tipi!";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    class Validator implements ConstraintValidator<CompatibleType, Asset> {

        @Override
        public boolean isValid(Asset asset, ConstraintValidatorContext constraintValidatorContext) {
            AssetMimeType mimeType = asset.getMimeType();
            return asset.getDomainType().isValid(mimeType);
        }
    }
}
