package com.example.backend.repositories;

import com.example.backend.models.Providers;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ProvidersRepository extends JpaRepository<Providers, UUID> {
}
