package com.toosterr.orderservice.service;

import com.toosterr.orderservice.dto.OrderRequest;
import com.toosterr.orderservice.model.Order;
import com.toosterr.orderservice.repository.OrderRepository;
import com.toosterr.orderservice.util.JwtUtil;
import com.toosterr.orderservice.model.Order;
import com.toosterr.orderservice.repository.OrderRepository;
import com.toosterr.orderservice.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

@Service
public class OrderService {

    private final WebClient webClient;
    private final JwtUtil jwtUtil;
    private final OrderRepository orderRepository;
    private final KafkaTemplate<String, String> kafkaTemplate;

    public OrderService(WebClient.Builder webClientBuilder, JwtUtil jwtUtil, OrderRepository orderRepository, KafkaTemplate<String, String> kafkaTemplate) {
        this.webClient = webClientBuilder.build();
        this.jwtUtil = jwtUtil;
        this.orderRepository = orderRepository;
        this.kafkaTemplate = kafkaTemplate;
    }

    public ResponseEntity<?> createOrder(OrderRequest orderRequest) {
        String url = "http://PRODUCT-SERVICE/api/v1/product/purchase/sku/{sku}";
        String newToken = jwtUtil.createToken();
        String newToken = jwtUtil.createToken();

        try {
            String data = webClient.get()
                    .uri(url, orderRequest.getSku())
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + newToken)
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + newToken)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

            if(data != null && data.equals("purchased")){
                //Add in order db
                Order order = Order.builder()
                        .sku(orderRequest.getSku())
                        .price(orderRequest.getPrice())
                        .quantity(orderRequest.getQuantity())
                        .userId(orderRequest.getUserId())
                        .build();
                orderRepository.save(order);
                kafkaTemplate.send("notificationTopic", "order created");
                //Add kafka message
            }

            return new ResponseEntity<>(data, HttpStatus.OK);

        } catch (WebClientResponseException.Unauthorized e) {
            return new ResponseEntity<>(e.getResponseBodyAsString(), HttpStatus.UNAUTHORIZED);
        } catch (WebClientResponseException.BadRequest e) {
            return new ResponseEntity<>(e, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}
