package com.toosterr.backend.util;

import org.springframework.core.NestedExceptionUtils;
import org.springframework.stereotype.Component;

@Component
public class Helper {

    public String getCodeFromString(String str, int length) {
        return str != null && str.length() >= length ? str.substring(0, length).toUpperCase() : str.toUpperCase();
    }

    public Throwable getRootCause(Throwable t) {
        Throwable rootCause = NestedExceptionUtils.getRootCause(t);
        return rootCause != null ? rootCause : t;
    }

}
