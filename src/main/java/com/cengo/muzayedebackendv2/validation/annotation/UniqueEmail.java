package com.cengo.muzayedebackendv2.validation.annotation;

import com.cengo.muzayedebackendv2.domain.entity.User;
import com.cengo.muzayedebackendv2.repository.UserRepository;
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
import java.util.UUID;

@Constraint(validatedBy = UniqueEmail.Validator.class)
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface UniqueEmail {
    String message() default "Bu e-mail ile ilişkili bir hesap zaten var!";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    class Validator implements ConstraintValidator<UniqueEmail, User> {
        @PersistenceContext
        private EntityManager entityManager;
        private final UserRepository userRepository;

        public Validator(UserRepository userRepository) {
            this.userRepository = userRepository;
        }

        @Override
        public boolean isValid(User user, ConstraintValidatorContext constraintValidatorContext) {
            UUID id = user.getId();
            String email = user.getEmail();

            entityManager.setFlushMode(FlushModeType.COMMIT);
            var existUser = userRepository.findByEmailIgnoreCase(email);
            entityManager.setFlushMode(FlushModeType.AUTO);

            return existUser.map(u -> u.getId().equals(id)).orElse(true);
        }
    }
}
