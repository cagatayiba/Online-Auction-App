package com.cengo.muzayedebackendv2.validation.user;

import com.cengo.muzayedebackendv2.domain.request.UserUpdatePasswordRequest;
import com.cengo.muzayedebackendv2.exception.message.ErrorMessage;
import com.cengo.muzayedebackendv2.validation.Validation;
import com.cengo.muzayedebackendv2.validation.ValidationContext;
import com.cengo.muzayedebackendv2.validation.ValidationStep;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
public class UserUpdatePasswordValidation implements Validation<UserUpdatePasswordValidation.Context> {

    @Override
    public void validate(Context context) {
        initialize()
                .linkWith(new PasswordMatchValidationStep())
                .validate(context);
    }

    private static class PasswordMatchValidationStep extends ValidationStep<Context> {
        @Override
        public void validate(Context context) {
            var newPassword = context.request.newPassword();
            var confirmPassword = context.request.confirmPassword();
            checkCondition(newPassword.equals(confirmPassword), ErrorMessage.PASSWORD_NOT_MATCH);
            checkNext(context);
        }
    }

    @AllArgsConstructor
    public static class Context implements ValidationContext {
        UserUpdatePasswordRequest request;
    }
}
