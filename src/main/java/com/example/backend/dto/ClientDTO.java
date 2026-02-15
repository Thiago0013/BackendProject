package com.example.backend.dto;

public record ClientDTO(
        String companyName,
        String cnpjNif,
        String address,
        String bio
) {
}
