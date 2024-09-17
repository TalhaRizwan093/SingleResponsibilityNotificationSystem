package com.toosterr.backend.exception.productException;

import lombok.Getter;

@Getter
public class ProductNotFoundException extends RuntimeException {
    private final Integer id;
    public ProductNotFoundException(String message, Integer id) {
        super(message);
        this.id = id;
    }

}
