package com.example.backend.services;

import com.example.backend.dto.ProviderDTO;
import com.example.backend.exceptions.ResourceNotFoundException;
import com.example.backend.models.Providers;
import com.example.backend.models.Users;
import com.example.backend.repositories.ProvidersRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class ProviderService {

    private final ProvidersRepository providersRepo;

    public ProviderService(ProvidersRepository providersRepo){
        this.providersRepo = providersRepo;
    }

    public Providers getProvider(Users user){
        return Optional.ofNullable(providersRepo.findByUser(user))
                .orElseThrow(() -> new ResourceNotFoundException("Prestador de serviço (Provider) não encontrado para este usuário."));
    }

    @Transactional
    public Providers editProvider(ProviderDTO dto, Users user){
        Providers provider = getProvider(user);

        if(dto.bio() != null) provider.setBio(dto.bio());
        if(dto.hourlyRate() != null) provider.setHourlyRate(dto.hourlyRate());
        if(dto.portifolioURL() != null) provider.setPortifolioURL(dto.portifolioURL());
        if(dto.skills() != null) provider.setSkills(dto.skills());

        return providersRepo.save(provider);
    }
}
