package com.toosterr.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatusCode;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomErrorResponse {

    private String message;
    private HttpStatusCode statusCode;
    private Map<String, String> errors;
    private String errorType;

    public CustomErrorResponse(String message, HttpStatusCode statusCode, String errorType) {
        this.message = message;
        this.statusCode = statusCode;
        this.errorType = errorType;
    }

}
