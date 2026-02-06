package com.cengo.muzayedebackendv2.validation;

import com.cengo.muzayedebackendv2.exception.BadRequestException;
import com.cengo.muzayedebackendv2.exception.message.BaseErrorMessage;

public abstract class ValidationStep<T> {
    private ValidationStep<T> next;

    public ValidationStep<T> linkWith(ValidationStep<T> next) {
        if (this.next == null) {
            this.next = next;
            return this;
        }
        ValidationStep<T> lastStep = this.next;
        while (lastStep.next != null) {
            lastStep = lastStep.next;
        }
        lastStep.next = next;
        return this;
    }

    public abstract void validate(T context);

    protected void checkNext(T context) {
        if (next != null) {
            next.validate(context);
        }
    }

    protected void checkCondition(Boolean condition, BaseErrorMessage message) {
        if (Boolean.FALSE.equals(condition)) throw new BadRequestException(message);
    }
}
