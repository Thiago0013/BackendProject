package com.example.backend.repositories;

import com.example.backend.models.Cliente;
import com.example.backend.models.Projects;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ProjectRepository extends JpaRepository<Projects, UUID> {
    List<Projects> findAllByCliente(Cliente client);

    boolean existsByTitle(String title);
}
