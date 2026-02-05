package com.example.backend.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "negotiations")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Negotiations {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private BigDecimal proposedValue;
    private String status;
    private LocalDateTime createdAt;


    private Projects projects;
}
