package com.cengo.muzayedebackendv2.exception.advicer;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.cengo.muzayedebackendv2.exception.advicer.response.ErrorResponse;
import com.cengo.muzayedebackendv2.exception.advicer.response.FieldErrorResponse;
import com.cengo.muzayedebackendv2.exception.base.BaseException;
import com.cengo.muzayedebackendv2.exception.message.BaseErrorMessage;
import com.cengo.muzayedebackendv2.exception.message.ErrorMessage;
import com.cengo.muzayedebackendv2.exception.message.LoginErrorMessage;
import jakarta.validation.ConstraintViolationException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;

import java.time.LocalDateTime;
import java.util.List;

@ControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class GlobalExceptionController {

    @ExceptionHandler({BaseException.class})
    public ResponseEntity<ErrorResponse> handleException(BaseException ex, WebRequest request) {
        var baseErrorMessage = ex.getBaseErrorMessage();
        return getErrorResponse(baseErrorMessage.getTitle(), baseErrorMessage.getMessage(), ex.getHttpStatus(), request);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<FieldErrorResponse> handleValidationErrors(MethodArgumentNotValidException ex, WebRequest request) {
        var errors = ex.getFieldErrors().stream()
                .map(violation -> violation.getField() + ": " + violation.getDefaultMessage())
                .toList();

        return getFieldErrorResponse(errors, request);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<FieldErrorResponse> handleValidationErrors(ConstraintViolationException ex, WebRequest request) {
        var errors = ex.getConstraintViolations().stream()
                .map(violation -> {
                    if (violation.getPropertyPath().toString().isBlank()){
                        return violation.getMessage();
                    }
                    return violation.getPropertyPath().toString() + ": " + violation.getMessage();
                })
                .toList();

        return getFieldErrorResponse(errors, request);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorResponse> handleAuthenticationException(WebRequest request) {
        var baseErrorMessage = LoginErrorMessage.INVALID_PASSWORD;
        return getErrorResponse(baseErrorMessage.getTitle(), baseErrorMessage.getMessage(), HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler({AccessDeniedException.class, InsufficientAuthenticationException.class, DisabledException.class})
    public ResponseEntity<ErrorResponse> handleForbiddenException(Exception ex, WebRequest request) {
        BaseErrorMessage baseErrorMessage = switch (ex) {
            case AccessDeniedException ignored -> ErrorMessage.FORBIDDEN_REQUEST;
            case InsufficientAuthenticationException ignored -> ErrorMessage.FORBIDDEN_REQUEST;
            case DisabledException ignored -> LoginErrorMessage.NOT_APPROVED;
            default -> throw new IllegalStateException("Unexpected exception: " + ex);
        };

        return getErrorResponse(baseErrorMessage.getTitle(), baseErrorMessage.getMessage(), HttpStatus.FORBIDDEN, request);
    }

    @ExceptionHandler(TokenExpiredException.class)
    public ResponseEntity<ErrorResponse> handleTokenExpiredException(WebRequest request) {
        var baseErrorMessage = ErrorMessage.TOKEN_EXPIRED;
        return getErrorResponse(baseErrorMessage.getTitle(), baseErrorMessage.getMessage(), HttpStatus.UNAUTHORIZED, request);
    }

    @ExceptionHandler(JWTVerificationException.class)
    public ResponseEntity<ErrorResponse> handleJWTVerificationException(WebRequest request) {
        var baseErrorMessage = ErrorMessage.TOKEN_INVALID;
        return getErrorResponse(baseErrorMessage.getTitle(), baseErrorMessage.getMessage(), HttpStatus.FORBIDDEN, request);
    }

    @ExceptionHandler({MultipartException.class, MissingServletRequestPartException.class})
    public ResponseEntity<ErrorResponse> handleMultipartException(WebRequest request) {
        var baseErrorMessage = ErrorMessage.MULTIPART_FILE;
        return getErrorResponse(baseErrorMessage.getTitle(), baseErrorMessage.getMessage(), HttpStatus.BAD_REQUEST, request);
    }


    private ResponseEntity<ErrorResponse> getErrorResponse(String title, String message,
                                                               HttpStatus httpStatus, WebRequest request) {
        var errorResponse = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .title(title)
                .message(message)
                .description(request.getDescription(false))
                .httpCode(httpStatus.value())
                .build();

        return new ResponseEntity<>(errorResponse, new HttpHeaders(), httpStatus);
    }

    private ResponseEntity<FieldErrorResponse> getFieldErrorResponse(List<String> errors, WebRequest request) {
        var errorResponse = FieldErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .errors(errors)
                .description(request.getDescription(false))
                .httpCode(HttpStatus.BAD_REQUEST.value())
                .build();

        return new ResponseEntity<>(errorResponse, new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }
}
