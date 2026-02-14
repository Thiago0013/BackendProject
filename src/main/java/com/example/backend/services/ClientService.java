package com.example.backend.services;

import com.example.backend.dto.ClientDTO;
import com.example.backend.models.Cliente;
import com.example.backend.models.Users;
import com.example.backend.repositories.ClienteRepository;
import org.springframework.stereotype.Service;

@Service
public class ClientService {

    private final ClienteRepository clientRepo;

    public ClientService(ClienteRepository clientRepo){
        this.clientRepo = clientRepo;
    }

    public Cliente getClient(Users user){
        if(!clientRepo.existsByUser(user)){
            throw new RuntimeException("ERRO: Usuario não existe");
        }
        return clientRepo.findByUser(user);
    }

    public Cliente editClient(ClientDTO dto, Users user){
        if(!clientRepo.existsByUser(user)){
            throw new RuntimeException("ERRO: Usuario não existe");
        }
        Cliente client = clientRepo.findByUser(user);
        if(dto.address() != null){
            client.setAddress(dto.address());
        }
        if(dto.cnpjNif() != null) {
            client.setCnpjNif(dto.cnpjNif());
        }
        if(dto.companyName() != null){
            client.setCompanyName(dto.companyName());
        }

        return clientRepo.save(client);
    }
}
