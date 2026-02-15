package com.example.backend.controllers;

import com.example.backend.dto.ProviderDTO;
import com.example.backend.models.Providers;
import com.example.backend.models.Users;
import com.example.backend.services.ProviderService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/provider")
public class ProviderController {
    private final ProviderService providerService;

    public ProviderController(ProviderService providerService){
        this.providerService = providerService;
    }

    @GetMapping
    public ResponseEntity<Providers> getProviders(@AuthenticationPrincipal Users user){
        Providers provider = providerService.getProvider(user);
        return ResponseEntity.ok(provider);
    }

    @PutMapping
    public ResponseEntity<Providers> putProviders(@RequestBody ProviderDTO dto, @AuthenticationPrincipal Users user){
        Providers provider = providerService.editProvider(dto, user);
        return ResponseEntity.ok(provider);
    }
}
