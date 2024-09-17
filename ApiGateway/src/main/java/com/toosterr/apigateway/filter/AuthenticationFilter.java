package com.toosterr.apigateway.filter;

import com.toosterr.apigateway.exception.user.UnauthorizedUser;
import com.toosterr.apigateway.util.JwtUtil;
import io.jsonwebtoken.Claims;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AuthenticationFilter extends AbstractGatewayFilterFactory<AuthenticationFilter.Config> {

    private final RouteValidator validator;
    private final JwtUtil jwtUtil;

    public AuthenticationFilter(RouteValidator validator, JwtUtil jwtUtil) {
        super(Config.class);
        this.validator = validator;
        this.jwtUtil = jwtUtil;
    }

    @Override
    public GatewayFilter apply(AuthenticationFilter.Config config) {
        return (exchange, chain) -> {
            if (!validator.isClosed.test(exchange.getRequest())) {
                if (!exchange.getRequest().getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
                    throw new UnauthorizedUser("Missing authorization header");
                }

                String authHeader = exchange.getRequest().getHeaders().get(HttpHeaders.AUTHORIZATION).get(0);
                if (authHeader != null && authHeader.startsWith("Bearer ")) {
                    authHeader = authHeader.substring(7);
                } else {
                    throw new UnauthorizedUser("Invalid authorization header format");
                }

                jwtUtil.validateToken(authHeader);
                List<String> userRoles = jwtUtil.extractRoles(authHeader);
                String path = exchange.getRequest().getURI().getPath();
                List<String> requiredRoles = validator.getRequiredRoles(path);
                boolean hasRole = userRoles.stream().anyMatch(requiredRoles::contains);
                if (!hasRole) {
                    throw new UnauthorizedUser("Insufficient permissions");
                }
            }

            String newToken = jwtUtil.createToken();

            exchange.getRequest().mutate()
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + newToken)
                    .build();

            return chain.filter(exchange);
        };
    }

    public static class Config {}
}
