package com.toosterr.orderservice.service;

import com.toosterr.orderservice.dto.OrderRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

@Service
public class OrderService {

    private final RestTemplate restTemplate;
    private final WebClient webClient;

    public OrderService(RestTemplate restTemplate, WebClient.Builder webClientBuilder) {
        this.restTemplate = restTemplate;
        this.webClient = webClientBuilder.build();
    }

    public ResponseEntity<?> createOrder(OrderRequest orderRequest) {
        String url = "http://PRODUCT-SERVICE/api/v1/product/purchase/sku/{sku}";

        try {
            String data = webClient.get()
                    .uri(url, orderRequest.getSku())
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

            return new ResponseEntity<>(data, HttpStatus.OK);

        } catch (WebClientResponseException.Unauthorized e) {
            // Handle unauthorized access error
            return new ResponseEntity<>(e.getResponseBodyAsString(), HttpStatus.UNAUTHORIZED);
        } catch (WebClientResponseException.BadRequest e) {
            // Handle bad request error
            return new ResponseEntity<>(e, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            // Handle other exceptions
            return new ResponseEntity<>(e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}
