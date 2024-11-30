package com.toosterr.apigateway.filter;

import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

@Component
public class RouteValidator {

    public static final List<String> openApiEndpoints = List.of(
            "/eureka"
    );

    public Predicate<ServerHttpRequest> isClosed =
            request -> openApiEndpoints
                    .stream()
                    .anyMatch(uri -> request.getURI().getPath().equals(uri));

    private static final AntPathMatcher pathMatcher = new AntPathMatcher();

    public static final Map<String, List<String>> roleRequiredEndpoints = Map.of(
            "/api/v1/product/**", List.of("ROLE_USER", "ROLE_ADMIN"),
            "/api/v1/order/**", List.of("ROLE_USER")
    );

    public List<String> getRequiredRoles(String path) {
        return roleRequiredEndpoints.entrySet().stream()
                .filter(entry -> pathMatcher.match(entry.getKey(), path))
                .map(Map.Entry::getValue)
                .findFirst()
                .orElse(Collections.emptyList());
    }

}
