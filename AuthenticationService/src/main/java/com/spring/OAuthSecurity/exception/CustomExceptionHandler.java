package com.spring.OAuthSecurity.exception;

import com.spring.OAuthSecurity.dto.CustomErrorResponse;
import com.spring.OAuthSecurity.exception.user.DuplicateUserException;
import com.spring.OAuthSecurity.exception.user.UserNotFoundException;
import com.spring.OAuthSecurity.exception.user.UserRegistrationException;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConversionException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.nio.file.AccessDeniedException;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class CustomExceptionHandler {

    final MessageSource messageSource;

    public CustomExceptionHandler(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @ExceptionHandler(HttpMessageConversionException.class)
    public ResponseEntity<CustomErrorResponse> handleHttpMessageConversionException(HttpMessageConversionException ex) {
        Map<String, String> errors = new HashMap<>();
        String message = messageSource.getMessage("api.body.missing", new Object[]{ex.getMessage()}, LocaleContextHolder.getLocale());
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, message);
        CustomErrorResponse errorResponse = new CustomErrorResponse(HttpStatus.BAD_REQUEST, problemDetail, errors, "REQUEST_BODY_NOT_FOUND");
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        String message = messageSource.getMessage("api.param.missing", new Object[]{ex.getMessage()}, LocaleContextHolder.getLocale());
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, message);

        CustomErrorResponse errorResponse = new CustomErrorResponse(HttpStatus.BAD_REQUEST, problemDetail, errors, "ARGUMENT_NOT_VALID");

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UserRegistrationException.class)
    public ResponseEntity<ErrorResponse> handleUserRegistrationException(UserRegistrationException ex) {
        String message = messageSource.getMessage("api.user.registration", new Object[]{ex.getMessage()}, LocaleContextHolder.getLocale());
        return new ResponseEntity<>(ErrorResponse
                .create(ex, HttpStatus.BAD_REQUEST, message), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DuplicateUserException.class)
    public ResponseEntity<ErrorResponse> handleDuplicateUserException(DuplicateUserException ex) {
        String message = messageSource.getMessage("api.user.registration.duplicate", new Object[]{ex.getMessage()}, LocaleContextHolder.getLocale());
        return new ResponseEntity<>(ErrorResponse
                .create(ex, HttpStatus.BAD_REQUEST, message), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponse> userNotFoundException(UserNotFoundException ex) {
        String message = messageSource.getMessage("user.notFound", new Object[]{ex.getMessage()}, LocaleContextHolder.getLocale());
        return new ResponseEntity<>(ErrorResponse
                .create(ex, HttpStatus.NOT_FOUND, message), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(SignatureException.class)
    public ResponseEntity<ErrorResponse> handleSignatureException(SignatureException ex) {
        String message = messageSource.getMessage("jwt.signature.exception", new Object[]{ex.getMessage()}, LocaleContextHolder.getLocale());
        return new ResponseEntity<>(ErrorResponse
                .create(ex, HttpStatus.UNAUTHORIZED, message), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<ErrorResponse> handleExpiredJwtException(ExpiredJwtException ex) {
        String message = messageSource.getMessage("jwt.expired.exception", new Object[]{ex.getMessage()}, LocaleContextHolder.getLocale());
        return new ResponseEntity<>(ErrorResponse
                .create(ex, HttpStatus.UNAUTHORIZED, message), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(MalformedJwtException.class)
    public ResponseEntity<ErrorResponse> handleMalformedJwtException(MalformedJwtException ex) {
        String message = messageSource.getMessage("jwt.malformed.exception", new Object[]{ex.getMessage()}, LocaleContextHolder.getLocale());
        return new ResponseEntity<>(ErrorResponse
                .builder(ex, HttpStatus.BAD_REQUEST, message)
                .title("JWT Exception")
                .build()
                , HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UnsupportedJwtException.class)
    public ResponseEntity<ErrorResponse> handleUnsupportedJwtException(UnsupportedJwtException ex) {
        String message = messageSource.getMessage("jwt.unsupported.exception", new Object[]{ex.getMessage()}, LocaleContextHolder.getLocale());
        return new ResponseEntity<>(ErrorResponse
                .create(ex, HttpStatus.BAD_REQUEST, message), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgumentException(IllegalArgumentException ex) {
        String message = messageSource.getMessage("illegal.argument.exception", new Object[]{ex.getMessage()}, LocaleContextHolder.getLocale());
        return new ResponseEntity<>(ErrorResponse
                .create(ex, HttpStatus.BAD_REQUEST, message), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ErrorResponse> onMissingParameterException(
            MissingServletRequestParameterException ex) {
        String param = ex.getParameterName();
        String message =
                messageSource.getMessage(
                        "api.param.missing", new Object[] {param}, LocaleContextHolder.getLocale());
        return new ResponseEntity<>(ErrorResponse
                .create(ex, HttpStatus.BAD_REQUEST, message), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> onTypeMismatchException(
            MethodArgumentTypeMismatchException ex) {
        String message =
                messageSource.getMessage(
                        "api.type.mismatch",
                        new Object[] {ex.getName(), ex.getRequiredType().getName()},
                        LocaleContextHolder.getLocale());
        return new ResponseEntity<>(ErrorResponse
                .create(ex, HttpStatus.BAD_REQUEST, message), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public  ResponseEntity<ErrorResponse> onBadCredentialsException(BadCredentialsException ex){
        String message = messageSource.getMessage("auth.credentials.exception", new Object[]{ex.getMessage()}, LocaleContextHolder.getLocale());
        return new ResponseEntity<>(ErrorResponse
                .create(ex, HttpStatus.UNAUTHORIZED, message), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ErrorResponse> handleAuthenticationException(Exception ex) {
        String message = messageSource.getMessage("authentication.exception", new Object[]{ex.getMessage()}, LocaleContextHolder.getLocale());
        return new ResponseEntity<>(ErrorResponse
                .create(ex, HttpStatus.UNAUTHORIZED, message), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(HttpClientErrorException.Forbidden.class)
    public ResponseEntity<ErrorResponse> handleForbiddenException(Exception ex) {
        String message = messageSource.getMessage("forbidden.exception", new Object[]{ex.getMessage()}, LocaleContextHolder.getLocale());
        return new ResponseEntity<>(ErrorResponse
                .create(ex, HttpStatus.UNAUTHORIZED, message), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> handleAccessDeniedException(Exception ex) {
        String message = messageSource.getMessage("access.denied.exception", new Object[]{ex.getMessage()}, LocaleContextHolder.getLocale());
        return new ResponseEntity<>(ErrorResponse
                .create(ex, HttpStatus.UNAUTHORIZED, message), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(AuthorizationDeniedException.class)
    public ResponseEntity<ErrorResponse> handleAuthorizationDeniedException(Exception ex) {
        String message = messageSource.getMessage("authorization.denied.exception", new Object[]{ex.getMessage()}, LocaleContextHolder.getLocale());
        return new ResponseEntity<>(ErrorResponse
                .create(ex, HttpStatus.UNAUTHORIZED, message), HttpStatus.UNAUTHORIZED);
    }

}
