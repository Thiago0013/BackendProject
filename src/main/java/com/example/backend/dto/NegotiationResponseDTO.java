package com.example.backend.dto;


import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record NegotiationResponseDTO(
        UUID id,
        BigDecimal proposedValue,
        String message,
        String status,
        LocalDateTime createdAt,
        String providerName,
        String providerEmail,
        String providerPhone
) {}