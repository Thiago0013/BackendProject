package com.example.backend.services;

import com.example.backend.dto.ClientDTO;
import com.example.backend.exceptions.ResourceNotFoundException;
import com.example.backend.models.Cliente;
import com.example.backend.models.Users;
import com.example.backend.repositories.ClienteRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ClientService {

    private final ClienteRepository clientRepo;

    public ClientService(ClienteRepository clientRepo){
        this.clientRepo = clientRepo;
    }

    public Cliente getClient(Users user) {
        return Optional.ofNullable(clientRepo.findByUser(user))
                .orElseThrow(() -> new ResourceNotFoundException("Cliente não encontrado para este usuário."));
    }

    public Cliente editClient(ClientDTO dto, Users user){
        Cliente client = Optional.ofNullable(clientRepo.findByUser(user))
                .orElseThrow(() -> new ResourceNotFoundException("Cliente não encontrado para este usuário."));
        if(dto.address() != null){
            client.setAddress(dto.address());
        }
        if(dto.cnpjNif() != null) {
            client.setCnpjNif(dto.cnpjNif());
        }
        if(dto.bio() != null) {
            client.setBio(dto.bio());
        }
        if(dto.companyName() != null){
            client.setCompanyName(dto.companyName());
        }

        return clientRepo.save(client);
    }
}
