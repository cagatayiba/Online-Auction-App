package com.cengo.muzayedebackendv2.validation.annotation;

import com.cengo.muzayedebackendv2.domain.entity.Auction;
import com.cengo.muzayedebackendv2.domain.entity.enums.AuctionState;
import com.cengo.muzayedebackendv2.repository.AuctionRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.FlushModeType;
import jakarta.persistence.PersistenceContext;
import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = NonConflictAuction.Validator.class)
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface NonConflictAuction {
    String message() default "Müzayede başlangıç tarihi başka müzayedelerin bitiş tarihinden önce olamaz!";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    class Validator implements ConstraintValidator<NonConflictAuction, Auction> {
        @PersistenceContext
        private EntityManager entityManager;
        private final AuctionRepository auctionRepository;

        public Validator(AuctionRepository auctionRepository) {
            this.auctionRepository = auctionRepository;
        }

        @Override
        public boolean isValid(Auction auction, ConstraintValidatorContext constraintValidatorContext) {
            if (auction.getState() != AuctionState.READY) return true;

            entityManager.setFlushMode(FlushModeType.COMMIT);
            var exists = auctionRepository.existsByEndTimeAfterAndStateNotAndIdNot(auction.getStartTime(), AuctionState.DRAFT, auction.getId());
            entityManager.setFlushMode(FlushModeType.AUTO);

            return  !exists;
        }
    }
}
