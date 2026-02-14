package com.example.backend.controllers;

import com.example.backend.dto.ClientDTO;
import com.example.backend.models.Cliente;
import com.example.backend.models.Users;
import com.example.backend.services.ClientService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/client")
public class ClientController {

    private final ClientService clientService;

    public ClientController(ClientService clientService){
        this.clientService = clientService;
    }

    @GetMapping
    public ResponseEntity<Cliente> getClient(@AuthenticationPrincipal Users user){
        Cliente client = clientService.getClient(user);
        return ResponseEntity.ok(client);
    }

    @PutMapping
    public ResponseEntity<Cliente> putClient(@RequestBody ClientDTO dto, @AuthenticationPrincipal Users user){
        Cliente client = clientService.editClient(dto, user);
        return ResponseEntity.ok(client);
    }
}
