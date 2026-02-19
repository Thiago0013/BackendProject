package com.example.backend.services;

import com.example.backend.dto.NegotiationResponseDTO;
import com.example.backend.dto.ProjectDTO;
import com.example.backend.dto.ProjectResponseDTO;
import com.example.backend.dto.ProjectWithClientDTO;
import com.example.backend.exceptions.BusinessException;
import com.example.backend.exceptions.ResourceNotFoundException;
import com.example.backend.exceptions.UnauthorizedAccessException;
import com.example.backend.models.Cliente;
import com.example.backend.models.Negotiations;
import com.example.backend.models.Projects;
import com.example.backend.models.Users;
import com.example.backend.models.enums.StatusProjectType;
import com.example.backend.models.enums.StatusType;
import com.example.backend.repositories.ClienteRepository;
import com.example.backend.repositories.NegotiationsRepository;
import com.example.backend.repositories.ProjectRepository;
import com.example.backend.repositories.ProvidersRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class ProjectService {

    private final ProjectRepository projectRepo;
    private final ClienteRepository clientRepo;
    private final ProvidersRepository providersRepo;
    private final NegotiationsRepository negotiationsRepo;

    public ProjectService(ProjectRepository projectRepo,
                          ClienteRepository clientRepo,
                          NegotiationsRepository negotiationsRepo,
                          ProvidersRepository providersRepo){
        this.projectRepo = projectRepo;
        this.clientRepo = clientRepo;
        this.negotiationsRepo = negotiationsRepo;
        this.providersRepo = providersRepo;
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
            throw new BusinessException("Já existe um projeto cadastrado com este título.");
        }

        Projects newProjects = new Projects();
        newProjects.setTitle(dto.title());
        newProjects.setDescription(dto.description());
        newProjects.setStatus(StatusProjectType.OPEN);
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

    public ProjectResponseDTO update(UUID projectId, ProjectDTO dto, Users user){
        Projects project = validateProjectOwnership(projectId, user);

        if(dto.title() != null){
            project.setTitle(dto.title());
        }
        if (dto.description() != null){
            project.setDescription(dto.description());
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

        Projects project = validateProjectOwnership(id, user);
        projectRepo.delete(project);
    }

    public List<NegotiationResponseDTO> listProposalsForClient(UUID projectId, Users user) {
        Projects project = validateProjectOwnership(projectId, user);

        return negotiationsRepo.findAllByProjects(project).stream()
                .map(n -> new NegotiationResponseDTO(
                        n.getId(),
                        n.getProposedValue(),
                        n.getMessage(),
                        n.getStatus().toString(),
                        n.getCreatedAt(),
                        n.getProviders().getUser().getName(),
                        n.getProviders().getUser().getEmail(),
                        n.getProviders().getUser().getPhone()
                ))
                .toList();
    }

    @Transactional
    public void completed(UUID projectId, Users user){
        Projects projects = validateProjectOwnership(projectId, user);

        if(projects.getStatus() == StatusProjectType.CLOSE){
            throw new BusinessException("Este projeto está fechado");
        }

        Negotiations findNegotiation = projects.getNegotiations().stream()
                .filter(n -> n.getStatus().equals(StatusType.ACCEPTED))
                .findFirst()
                .orElseThrow(() -> new BusinessException("Nenhuma negociação aceita encontrada."));

        projects.setStatus(StatusProjectType.COMPLETED);
        findNegotiation.setStatus(StatusType.FINISHED);

        negotiationsRepo.save(findNegotiation);
        projectRepo.save(projects);
    }

    public void close(UUID projectId, Users user){

        Projects project = validateProjectOwnership(projectId, user);

        if(project.getStatus() == StatusProjectType.COMPLETED){
            throw new BusinessException("Este projeto já está concluido.");
        }

        project.setStatus(StatusProjectType.CLOSE);
        projectRepo.save(project);
    }

    @Transactional
    public void open(UUID projectId, Users user){

        Projects project = validateProjectOwnership(projectId, user);

        if(project.getStatus() == StatusProjectType.COMPLETED){
            throw new BusinessException("Este projeto já está concluido.");
        }

        project.setStatus(StatusProjectType.OPEN);
        projectRepo.save(project);
    }

    private Projects validateProjectOwnership(UUID projectId, Users user){
        if(providersRepo.existsByUser(user)){
            throw new UnauthorizedAccessException("Acesso negado: esta área é exclusiva para clientes.");
        }

        Projects projects = projectRepo.findById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Projeto não encontrada. Tente com uma outro projeto ou id diferente. Id: " + projectId));

        if(!projects.getCliente().getUser().getId().equals(user.getId())){
            throw new UnauthorizedAccessException("Acesso negado: este projeto não pertence a você.");
        }

        return projects;
    }
}
