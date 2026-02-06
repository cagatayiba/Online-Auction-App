package com.cengo.muzayedebackendv2.exception.advicer;

import com.cengo.muzayedebackendv2.exception.advicer.response.ErrorResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.time.LocalDateTime;

@ControllerAdvice
@Order
public class InternalExceptionController {
    private final Logger logger = LoggerFactory.getLogger(InternalExceptionController.class);

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<Object> handleNoResourceFoundException() {
        return new ResponseEntity<>(new HttpHeaders(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception ex, WebRequest request) {
        logger.error("Internal server error: ", ex);
        return getErrorResponse(request);
    }

    private ResponseEntity<ErrorResponse> getErrorResponse(WebRequest request) {
        var errorResponse = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .title("Internal Server Error")
                .message("Bir şeyler ters gitti.")
                .description(request.getDescription(false))
                .httpCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .build();

        return new ResponseEntity<>(errorResponse, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
