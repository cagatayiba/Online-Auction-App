package com.cengo.muzayedebackendv2.exception;

import com.cengo.muzayedebackendv2.exception.base.BaseException;
import com.cengo.muzayedebackendv2.exception.message.ErrorMessage;
import org.springframework.http.HttpStatus;

public class MailNotSendException extends BaseException {
    public MailNotSendException() {
        super(HttpStatus.BAD_GATEWAY, ErrorMessage.MAIL_NOT_SEND);
    }
}
