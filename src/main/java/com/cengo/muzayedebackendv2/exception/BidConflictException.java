package com.cengo.muzayedebackendv2.exception;

import com.cengo.muzayedebackendv2.exception.base.BaseException;
import com.cengo.muzayedebackendv2.exception.message.BidErrorMessage;
import org.springframework.http.HttpStatus;

public class BidConflictException extends BaseException {
    public BidConflictException() {
        super(HttpStatus.CONFLICT, BidErrorMessage.BID_CONFLICT);
    }
}
