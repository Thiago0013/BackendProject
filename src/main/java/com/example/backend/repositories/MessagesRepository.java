package com.example.backend.repositories;

import com.example.backend.models.Messages;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface MessagesRepository extends JpaRepository<Messages, UUID> {
}
