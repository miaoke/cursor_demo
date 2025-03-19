package org.example.entity;

import java.math.BigDecimal;
import lombok.Data;

@Data
public class Order {
    private Long orderId;
    private Long userId;
    private BigDecimal amount;
    private String status;
    private Long createdAt;
} 