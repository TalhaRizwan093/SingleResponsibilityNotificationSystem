package com.toosterr.backend.exception;

import com.toosterr.backend.dto.CustomErrorResponse;
import com.toosterr.backend.exception.categoryException.CategoryNotFoundException;
import com.toosterr.backend.exception.productException.ProductNotFoundException;
import com.toosterr.backend.util.Constant;
import com.toosterr.backend.util.Helper;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private final MessageSource messageSource;
    private final Helper helper;

    public GlobalExceptionHandler(MessageSource messageSource, Helper helper) {
        this.messageSource = messageSource;
        this.helper = helper;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        String message = messageSource.getMessage("api.param.invalid", new Object[]{ex.getMessage()}, LocaleContextHolder.getLocale());
        CustomErrorResponse errorResponse = new CustomErrorResponse(message, HttpStatus.BAD_REQUEST, errors, "ARGUMENT_NOT_VALID");
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(CategoryNotFoundException.class)
    public ResponseEntity<?> handleCategoryNotFoundException(CategoryNotFoundException ex) {
        String message = messageSource.getMessage("category.not.found", new Object[]{ex.getMessage()}, LocaleContextHolder.getLocale());
        CustomErrorResponse errorResponse = new CustomErrorResponse(message,HttpStatus.INTERNAL_SERVER_ERROR,"INTERNAL_SERVER_ERROR");
        return  new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<?> handleRuntimeException(RuntimeException ex) {
        String message = messageSource.getMessage("internal.server.error", new Object[]{ex.getMessage()}, LocaleContextHolder.getLocale());
        Map<String, String> errors = new HashMap<>();
        errors.put(ex.getCause().getMessage(), ex.getMessage());

        CustomErrorResponse errorResponse = new CustomErrorResponse(message,HttpStatus.INTERNAL_SERVER_ERROR, errors,"INTERNAL_SERVER_ERROR");
        return  new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<?> conflict(DataIntegrityViolationException ex) {
        String rootMsg = helper.getRootCause(ex).getMessage();
        for (Map.Entry<String, String> entry : Constant.CONSTRAINS_I18N_MAP.entrySet()) {
            if (rootMsg.contains(entry.getKey())) {
                String message = messageSource.getMessage(entry.getValue(), new Object[]{ex.getMessage()}, LocaleContextHolder.getLocale());
                CustomErrorResponse errorResponse = new CustomErrorResponse(message,HttpStatus.CONFLICT,"UNIQUE_CONSTRAINT_VIOLATION");
                return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
            }
        }

        String message = messageSource.getMessage("data.integrity.exception", new Object[]{ex.getMessage()}, LocaleContextHolder.getLocale());
        CustomErrorResponse errorResponse = new CustomErrorResponse(message,HttpStatus.BAD_REQUEST,"DATA_INTEGRITY_VIOLATION");
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<?> handleProductNotFoundException(ProductNotFoundException ex) {
        String message = messageSource.getMessage("product.not.found", new Object[]{ex.getId()}, LocaleContextHolder.getLocale());
        Map<String, String> errors = new HashMap<>();
        if (ex.getCause() != null) {
            errors.put(ex.getCause().getMessage(), ex.getMessage());
        }
        CustomErrorResponse errorResponse = new CustomErrorResponse(message,HttpStatus.BAD_REQUEST, errors,"PRODUCT_NOT_FOUND");
        return  new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

}
