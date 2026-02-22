package com.example.backend.dto;

import com.example.backend.models.Projects;
import com.example.backend.models.enums.StatusType;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record NegotiationsResponseDTO(
        UUID id,
        String message,
        BigDecimal proposedValue,
        StatusType status,
        LocalDateTime createdAt,
        UUID projectId,
        String projectTitle,
        String projectDescription,
        LocalDateTime projectCreateAt
){
}
