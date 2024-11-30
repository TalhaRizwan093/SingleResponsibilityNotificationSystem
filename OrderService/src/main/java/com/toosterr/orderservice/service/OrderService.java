package com.toosterr.orderservice.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.toosterr.orderservice.dto.OrderNotification;
import com.toosterr.orderservice.dto.OrderRequest;
import com.toosterr.orderservice.model.Order;
import com.toosterr.orderservice.repository.OrderRepository;
import com.toosterr.orderservice.util.JwtUtil;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

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
                OrderNotification orderNotification = OrderNotification.builder()
                        .orderId(order.getOrderId())
                        .customerId(order.getUserId())
                        .productSku(order.getSku())
                        .build();
                ObjectMapper objectMapper = new ObjectMapper();
                String orderNotificationJson = objectMapper.writeValueAsString(orderNotification);
                kafkaTemplate.send("notificationTopic", orderNotificationJson);
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
