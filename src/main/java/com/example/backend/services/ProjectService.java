package com.example.backend.services;

import com.example.backend.dto.ProjectDTO;
import com.example.backend.dto.ProjectResponseDTO;
import com.example.backend.dto.ProjectWithClientDTO;
import com.example.backend.models.Cliente;
import com.example.backend.models.Projects;
import com.example.backend.models.Users;
import com.example.backend.repositories.ClienteRepository;
import com.example.backend.repositories.ProjectRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class ProjectService {

    private final ProjectRepository projectRepo;
    private final ClienteRepository clientRepo;

    public ProjectService(ProjectRepository projectRepo, ClienteRepository clientRepo){
        this.projectRepo = projectRepo;
        this.clientRepo = clientRepo;
    }

    public List<ProjectWithClientDTO> getAll(){
        return projectRepo.findAll().stream()
                .map(p -> new ProjectWithClientDTO(
                        p.getId(),
                        p.getTitle(),
                        p.getDescription(),
                        p.getBudget(),
                        p.getStatus(),
                        p.getDeadline(),
                        p.getCliente().getUser().getName(),
                        p.getCliente().getUser().getEmail()
                ))
                .toList();
    }

    public ProjectResponseDTO create(ProjectDTO dto, Users user){
        Cliente client = clientRepo.findByUser(user);

        if(projectRepo.existsByTitle(dto.title())){
            throw new RuntimeException("ERRO: esse projeto já existe!");
        }

        Projects newProjects = new Projects();
        newProjects.setTitle(dto.title());
        newProjects.setDescription(dto.description());
        newProjects.setStatus(dto.status());
        newProjects.setBudget(dto.budget());
        newProjects.setDeadline(dto.deadline());
        newProjects.setCliente(client);
        newProjects.setCreatedAt(LocalDateTime.now());

        projectRepo.save(newProjects);

        return new ProjectResponseDTO(
                newProjects.getId(),
                newProjects.getTitle(),
                newProjects.getDescription(),
                newProjects.getBudget(),
                newProjects.getStatus(),
                newProjects.getDeadline(),
                user.getName()
        );
    }

    public ProjectResponseDTO update(UUID id, ProjectDTO dto, Users user){
        Projects project = projectRepo.findById(id).orElseThrow(() -> new RuntimeException("ERRO: Projeto não encontrado!"));

        if(!project.getCliente().getUser().getId().equals(user.getId())){
            throw new RuntimeException("ERRO: Tarefa não pertence a esté usuario!");
        }

        if(dto.title() != null){
            project.setTitle(dto.title());
        }
        if (dto.description() != null){
            project.setDescription(dto.description());
        }
        if(dto.status() != null){
            project.setStatus(dto.status());
        }
        if(dto.budget() != null){
            project.setBudget(dto.budget());
        }
        if(dto.deadline() != null){
            project.setDeadline(dto.deadline());
        }

        projectRepo.save(project);

        return new ProjectResponseDTO(
                project.getId(),
                project.getTitle(),
                project.getDescription(),
                project.getBudget(),
                project.getStatus(),
                project.getDeadline(),
                project.getCliente().getUser().getName()
        );
    }

    public void delete(UUID id, Users user){
        Projects project = projectRepo.findById(id).orElseThrow(() -> new RuntimeException("ERRO: Projeto não encontrado!"));

        if(!project.getCliente().getUser().getId().equals(user.getId())){
            throw new RuntimeException("ERRO: Tarefa não pertece a esté usuario.");
        }

        projectRepo.delete(project);
    }

}
