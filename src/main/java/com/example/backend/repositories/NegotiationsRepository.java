package com.example.backend.repositories;

import com.example.backend.models.Negotiations;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface NegotiationsRepository extends JpaRepository<Negotiations, UUID> {
}
