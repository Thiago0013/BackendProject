package com.example.backend.services;

import com.example.backend.models.Projects;
import com.example.backend.repositories.ProjectRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProjectService {

    private final ProjectRepository projectRepo;

    public ProjectService(ProjectRepository projectRepo){
        this.projectRepo = projectRepo;
    }

    public List<Projects> getAll(){
        return projectRepo.findAll();
    }
}
