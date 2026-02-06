package com.example.backend.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record ProjectWithClientDTO(
        UUID id,
        String title,
        String description,
        BigDecimal budget,
        String status,
        LocalDateTime deadline,
        String clientUserName,
        String clientUserEmail
) {}
