package com.cengo.muzayedebackendv2.validation;



public interface Validation<T extends ValidationContext> {
    void validate(T context);

    default ValidationStep<T> initialize() {
        return new ValidationStep<>() {
            @Override
            public void validate(T context) {
                checkNext(context);
            }
        };
    }
}
