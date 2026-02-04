package com.example.backend.dto;

public record UsersDTO(
        String name,
        String email,
        String password,
        String phone
) {
}
