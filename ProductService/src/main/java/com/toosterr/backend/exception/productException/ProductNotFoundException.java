package com.toosterr.backend.exception.productException;

import lombok.Getter;

@Getter
public class ProductNotFoundException extends RuntimeException {
    private final String id;
    public ProductNotFoundException(String message, String id) {
        super(message);
        this.id = id;
    }

}
