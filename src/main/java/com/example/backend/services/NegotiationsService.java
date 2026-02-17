package com.example.backend.services;

import com.example.backend.dto.NegotiationsDTO;
import com.example.backend.models.Negotiations;
import com.example.backend.models.Projects;
import com.example.backend.models.Providers;
import com.example.backend.models.Users;
import com.example.backend.models.enums.StatusType;
import com.example.backend.repositories.NegotiationsRepository;
import com.example.backend.repositories.ProjectRepository;
import com.example.backend.repositories.ProvidersRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

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

    @Transactional
    public void saveNegotiations(UUID projectId, NegotiationsDTO dto, Users user){
        if(!providerRepo.existsByUser(user)){
            throw new RuntimeException("ERRO: Esta área é apenas para providers");
        }
        Providers provider = providerRepo.findByUser(user);

        Projects project = projectRepo.findById(projectId).orElseThrow(() -> new RuntimeException("ERRO: Projeto não encontrado."));
        if(project.getCliente().getUser().getId().equals(user.getId())){
            throw new RuntimeException("ERRO: Você não pode enviar propostas para si mesmo");
        }

        boolean alreadyHasProposal = negotiationsRepo.existsByProjectsAndProviders(project, provider);
        if(alreadyHasProposal){
            throw new RuntimeException("ERRO: Você já enviou propostas para esse projeto.");
        }

        Negotiations negotiations = new Negotiations();
        negotiations.setProposedValue(dto.proposedValue());
        negotiations.setMessage(dto.message());
        negotiations.setStatus(StatusType.PENDING);
        negotiations.setProjects(project);
        negotiations.setProviders(provider);

        negotiationsRepo.save(negotiations);
    }


}
