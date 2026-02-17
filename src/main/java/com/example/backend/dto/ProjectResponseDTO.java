package com.example.backend.dto;

import com.example.backend.models.enums.StatusProjectType;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record ProjectResponseDTO(
        UUID id,
        String title,
        String description,
        BigDecimal budget,
        StatusProjectType status,
        LocalDateTime deadline,
        String clientName
) {
}
