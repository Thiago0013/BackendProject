package com.example.backend.services;

import com.example.backend.dto.NegotiationsDTO;
import com.example.backend.dto.NegotiationsResponseDTO;
import com.example.backend.exceptions.BusinessException;
import com.example.backend.exceptions.ResourceNotFoundException;
import com.example.backend.exceptions.UnauthorizedAccessException;
import com.example.backend.models.Negotiations;
import com.example.backend.models.Projects;
import com.example.backend.models.Providers;
import com.example.backend.models.Users;
import com.example.backend.models.enums.StatusProjectType;
import com.example.backend.models.enums.StatusType;
import com.example.backend.repositories.NegotiationsRepository;
import com.example.backend.repositories.ProjectRepository;
import com.example.backend.repositories.ProvidersRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class NegotiationsService {
    private final NegotiationsRepository negotiationsRepo;
    private final ProjectRepository projectRepo;
    private final ProvidersRepository providerRepo;

    public NegotiationsService(NegotiationsRepository negotiationsRepo,
                               ProjectRepository projectRepo,
                               ProvidersRepository providerRepo
                               ){
        this.negotiationsRepo = negotiationsRepo;
        this.projectRepo = projectRepo;
        this.providerRepo = providerRepo;
    }

    public List<NegotiationsResponseDTO> getProviderNegotiations(Users user){
        validadeIsProvider(user);

        return negotiationsRepo.findAllByProviders(user.getProvider()).stream()
                .map(n -> new NegotiationsResponseDTO(
                        n.getId(),
                        n.getMessage(),
                        n.getProposedValue(),
                        n.getStatus(),
                        n.getCreatedAt(),
                        n.getProjects().getId(),
                        n.getProjects().getTitle(),
                        n.getProjects().getDescription(),
                        n.getProjects().getCreatedAt()
                )).toList();
    }

    @Transactional
    public void saveNegotiations(UUID projectId, NegotiationsDTO dto, Users user){
        validadeIsProvider(user);

        Providers provider = Optional.ofNullable(providerRepo.findByUser(user))
                .orElseThrow(() -> new ResourceNotFoundException("Provider não encontrado para este usuário."));

        Projects project = projectRepo.findById(projectId).orElseThrow(() -> new ResourceNotFoundException("Projeto não encontrado com o ID: " + projectId));

        if(project.getCliente().getUser().getId().equals(user.getId())){
            throw new BusinessException("Você não pode enviar propostas para o seu próprio projeto.");
        }

        boolean alreadyHasProposal = negotiationsRepo.existsByProjectsAndProviders(project, provider);
        if(alreadyHasProposal){
            throw new BusinessException("Você já enviou uma proposta para este projeto. Aguarde o retorno do cliente.");
        }

        Negotiations negotiations = new Negotiations();
        negotiations.setProposedValue(dto.proposedValue());
        negotiations.setMessage(dto.message());
        negotiations.setStatus(StatusType.PENDING);
        negotiations.setProjects(project);
        negotiations.setProviders(provider);

        negotiationsRepo.save(negotiations);
    }

    @Transactional
    public void accept(UUID id, Users user) {
        validateIsClient(user);

        Negotiations negotiation = negotiationsRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Negociação não encontrada. Tente com uma outra negociação ou id diferente. Id: " + id));

        if (!negotiation.getProjects().getCliente().getUser().getId().equals(user.getId())) {
            throw new UnauthorizedAccessException("Acesso negado: esta negociação não pertece a este usuario.");
        }

        negotiation.setStatus(StatusType.ACCEPTED);

        Projects project = negotiation.getProjects();
        project.setStatus(StatusProjectType.IN_PROCESS);

        negotiationsRepo.save(negotiation);
        projectRepo.save(project);
    }

    @Transactional
    public void denied(UUID id, Users user){
        validateIsClient(user);

        Negotiations negotiations = negotiationsRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Negociação não encontrada. Tente com uma outra negociação ou id diferente. Id: " + id));


        if(!negotiations.getProjects().getCliente().getUser().getId().equals(user.getId())){
            throw new UnauthorizedAccessException("Acesso negado: esta negociação não pertece a este usuario.");
        }

        negotiations.setStatus(StatusType.REJECTED);
        negotiationsRepo.save(negotiations);
    }


    public void validateIsClient(Users user){
        if(providerRepo.existsByUser(user)){
            throw new UnauthorizedAccessException("Acesso negado: esta área é exclusiva para clientes.");
        }
    }

    public void validadeIsProvider(Users user){
        if(!providerRepo.existsByUser(user)){
            throw new UnauthorizedAccessException("Acesso negado: esta área é exclusiva para providers (prestadores de serviço).");
        }
    }
}
