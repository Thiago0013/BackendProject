package com.example.backend.dto;

import com.example.backend.models.enums.UserType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UsersDTO(
        @NotBlank String name,
        @NotBlank @Email String email,
        @NotBlank String password,
        String phone,
        @NotNull UserType userType
) {
}
