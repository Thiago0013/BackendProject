package com.example.backend.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record NegotiationsDTO(
        @NotNull @Positive BigDecimal proposedValue,
        @NotNull String message
) {
}
