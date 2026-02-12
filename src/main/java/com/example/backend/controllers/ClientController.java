package com.example.backend.controllers;

import com.example.backend.models.Cliente;
import com.example.backend.models.Users;
import com.example.backend.services.ClientService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/client")
public class ClientController {

    private final ClientService clientService;

    public ClientController(ClientService clientService){
        this.clientService = clientService;
    }

    @GetMapping
    public ResponseEntity<Cliente> getClient(@AuthenticationPrincipal Users user){
        Cliente Client = clientService.getClient(user);
        return ResponseEntity.ok(Client);
    }
}
