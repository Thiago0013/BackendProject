package com.example.backend.services;

import com.example.backend.dto.ProviderDTO;
import com.example.backend.models.Providers;
import com.example.backend.models.Users;
import com.example.backend.repositories.ProvidersRepository;
import org.springframework.stereotype.Service;

@Service
public class ProviderService {

    private final ProvidersRepository providersRepo;

    public ProviderService(ProvidersRepository providersRepo){
        this.providersRepo = providersRepo;
    }

    public Providers getProvider(Users user){
        if(providersRepo.existsByUser(user)){
            throw new RuntimeException("ERRO: Não existe provider para este usuario.");
        }
        return providersRepo.findByUser(user);
    }

    public Providers editProvider(ProviderDTO dto, Users user){
        if(providersRepo.existsByUser(user)){
            throw new RuntimeException("ERRO: Não existe provider para este usuario.");
        }

        Providers provider = providersRepo.findByUser(user);

        if(dto.bio() != null){
            provider.setBio(dto.bio());
        }
        if(dto.hourlyRate() != null){
            provider.setHourlyRate(dto.hourlyRate());
        }
        if(dto.portifolioURL() != null){
            provider.setPortifolioURL(dto.portifolioURL());
        }
        if(dto.skills() != null){
            provider.setSkills(dto.skills());
        }

        return providersRepo.save(provider);
    }
}
