package com.example.backend.services;

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
}
