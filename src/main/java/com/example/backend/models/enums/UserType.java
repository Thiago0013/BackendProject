package com.example.backend.models.enums;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum UserType {
    @JsonProperty("client")
    CLIENT,

    @JsonProperty("provider")
    PROVIDER
}
