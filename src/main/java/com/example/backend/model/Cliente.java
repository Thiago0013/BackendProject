package com.example.backend.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "cliente")
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private Integer maxProposta;
    private Integer totalPropostas;
    private Double nota;

    @OneToOne
    private Users user;

}
