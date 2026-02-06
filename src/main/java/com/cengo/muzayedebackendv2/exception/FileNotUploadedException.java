package com.cengo.muzayedebackendv2.exception;

import com.cengo.muzayedebackendv2.exception.base.BaseException;
import com.cengo.muzayedebackendv2.exception.message.ErrorMessage;
import org.springframework.http.HttpStatus;

public class FileNotUploadedException extends BaseException {
    public FileNotUploadedException() {
        super(HttpStatus.BAD_GATEWAY, ErrorMessage.FILE_NOT_UPLOADED);
    }
}
