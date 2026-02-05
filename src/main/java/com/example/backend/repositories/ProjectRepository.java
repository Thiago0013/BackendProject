package com.example.backend.repositories;

import com.example.backend.models.Projects;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface ProjectRepository extends JpaRepository<Projects, UUID> {
    @Query("SELECT p FROM Projects p JOIN FETCH p.cliente")
    List<Projects> findAllWithCliente();

    boolean existsByTitle(String title);
}
