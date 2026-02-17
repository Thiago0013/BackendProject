package com.example.backend.repositories;

import com.example.backend.models.Negotiations;
import com.example.backend.models.Projects;
import com.example.backend.models.Providers;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface NegotiationsRepository extends JpaRepository<Negotiations, UUID> {
    boolean existsByProjectsAndProviders(Projects projects, Providers provider);

    List<Negotiations> findAllByProjects(Projects project);
}
