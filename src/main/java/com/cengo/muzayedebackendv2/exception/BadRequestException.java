package com.cengo.muzayedebackendv2.exception;

import com.cengo.muzayedebackendv2.exception.base.BaseException;
import com.cengo.muzayedebackendv2.exception.message.BaseErrorMessage;
import org.springframework.http.HttpStatus;

public class BadRequestException extends BaseException {

    public BadRequestException(BaseErrorMessage baseErrorMessage) {
        super(HttpStatus.BAD_REQUEST, baseErrorMessage);
    }
}
