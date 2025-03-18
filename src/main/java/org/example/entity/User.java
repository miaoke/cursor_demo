package org.example.entity;

import java.time.LocalDateTime;
import lombok.Data;

@Data
public class User {
    private Long userId;
    private String username;
    private String email;
    private LocalDateTime createdAt;
} 