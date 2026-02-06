package com.cengo.muzayedebackendv2.validation.annotation;

import com.cengo.muzayedebackendv2.domain.entity.WatchList;
import com.cengo.muzayedebackendv2.repository.WatchlistRepository;
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

@Constraint(validatedBy = UniqueWatchList.Validator.class)
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface UniqueWatchList {
    String message() default "Bu ürünü zaten favorilerinize eklediniz!";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    class Validator implements ConstraintValidator<UniqueWatchList, WatchList> {
        @PersistenceContext
        private EntityManager entityManager;
        private final WatchlistRepository watchlistRepository;

        public Validator(WatchlistRepository watchlistRepository) {
            this.watchlistRepository = watchlistRepository;
        }

        @Override
        public boolean isValid(WatchList watchlist, ConstraintValidatorContext constraintValidatorContext) {
            var id = watchlist.getId();
            var userId = watchlist.getUserId();
            var lotId = watchlist.getLotId();

            entityManager.setFlushMode(FlushModeType.COMMIT);
            var existWatchList = watchlistRepository.findByUserIdAndLotId(userId, lotId);
            entityManager.setFlushMode(FlushModeType.AUTO);

            return existWatchList.map(watchList -> watchList.getId().equals(id)).orElse(true);
        }
    }
}
