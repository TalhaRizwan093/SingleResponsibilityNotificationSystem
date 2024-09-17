package com.toosterr.orderservice.service;

import com.toosterr.orderservice.dto.OrderRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class OrderService {

    private final RestTemplate restTemplate;

    public OrderService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public ResponseEntity<?> createOrder(OrderRequest orderRequest) {
        // Decrease the quantity

        String url = "http://PRODUCT-SERVICE/api/v1/product/purchase/sku/{sku}"; // Replace with the actual URL
        try {
            var data = restTemplate.getForObject(url, String.class, orderRequest.getSku());
            // Process the response and handle the order creation logic
            return ResponseEntity.ok(data);
        } catch (Exception e) {
            // Handle the exception (e.g., logging, returning an error response)
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        // Save the product in db as non-tracked
        // Create Order it should consist of the following data {productId, orderId, UserId, quantity, price}
        //Save Order
        //Produce a notification to a kafka queue
        //return new ResponseEntity<>("Order Created Successfully", HttpStatus.OK);
    }

}
