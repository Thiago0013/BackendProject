package com.example.backend.repositories;

import com.example.backend.models.Providers;
import com.example.backend.models.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ProvidersRepository extends JpaRepository<Providers, UUID> {
    Providers findByUser(Users user);

    boolean existsByUser(Users user);
}
