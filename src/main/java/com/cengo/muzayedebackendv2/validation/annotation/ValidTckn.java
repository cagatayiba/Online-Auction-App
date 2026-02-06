package com.cengo.muzayedebackendv2.validation.annotation;

import com.cengo.muzayedebackendv2.client.tckn.TcknClient;
import com.cengo.muzayedebackendv2.client.tckn.TcknVerifyRequest;
import com.cengo.muzayedebackendv2.client.tckn.TcknVerifyResponse;
import com.cengo.muzayedebackendv2.domain.entity.User;
import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = ValidTckn.Validator.class)
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidTckn {
    String message() default "TC Kimlik no doğrulaması başarısız!";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    class Validator implements ConstraintValidator<ValidTckn, User> {
        private final TcknClient tcknClient;

        public Validator(TcknClient tcknClient) {
            this.tcknClient = tcknClient;
        }

        @Override
        public boolean isValid(User user, ConstraintValidatorContext constraintValidatorContext) {
            if (user.getIdNumber() == null){
                return true;
            }
            TcknVerifyRequest tcknVerifyRequest = new TcknVerifyRequest(user.getIdNumber(), user.getName(), user.getSurname(), user.getBirthDate().getYear());
            TcknVerifyResponse response = tcknClient.verify(tcknVerifyRequest);

            //TCKN whitelist
            if (user.getIdNumber() == 111L){
                response.getBody().getResponse().setSuccess(true);
            }
            return response.isSuccess();
        }
    }
}
