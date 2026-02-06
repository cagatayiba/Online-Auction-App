package com.cengo.muzayedebackendv2.validation.annotation;

import com.cengo.muzayedebackendv2.domain.entity.Auction;
import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.time.LocalDateTime;

@Constraint(validatedBy = ValidTimes.Validator.class)
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidTimes {
    String message() default "Bitiş zamanı, başlangıç zamanından sonra olmalı!";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    class Validator implements ConstraintValidator<ValidTimes, Auction> {

        @Override
        public boolean isValid(Auction auction, ConstraintValidatorContext constraintValidatorContext) {
            LocalDateTime start = auction.getStartTime();
            LocalDateTime end = auction.getEndTime();

            return start.isBefore(end);
        }
    }
}
