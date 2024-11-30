package com.toosterr.orderservice.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OrderNotification {

    @JsonSerialize
    private Integer orderId;
    @JsonSerialize
    private Integer customerId;
    @JsonSerialize
    private Integer productId;
    @JsonSerialize
    private String productSku;

}
