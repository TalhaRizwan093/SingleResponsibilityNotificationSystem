package com.toosterr.orderservice.config;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Configuration
public class AppConfig {

    private static final String SERVICE_HEADER_NAME = "X-Internal-Service";
    private static final String SERVICE_HEADER_VALUE = "OrderService"; // You can change this to the appropriate service name

    @Bean
    @LoadBalanced
    public WebClient.Builder webClientBuilder() {
        return WebClient.builder()
                .filter(addCustomHeader());
    }

    private ExchangeFilterFunction addCustomHeader() {
        return ExchangeFilterFunction.ofRequestProcessor(clientRequest -> {
            ClientRequest newRequest = ClientRequest.from(clientRequest)
                    .header(SERVICE_HEADER_NAME, SERVICE_HEADER_VALUE)
                    .build();
            return Mono.just(newRequest);
        });
    }

}
