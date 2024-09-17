package com.toosterr.backend.exception.attributeExcpetion;

public class ProductAttributeNotFoundException extends RuntimeException {
    public ProductAttributeNotFoundException(String message) {
        super(message);
    }
}
