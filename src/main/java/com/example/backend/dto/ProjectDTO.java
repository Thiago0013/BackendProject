package com.example.backend.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ProjectDTO(
        String title,
        String description,
        BigDecimal budget,
        String status,
        LocalDateTime deadline
) {
}
