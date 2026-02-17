package com.example.backend.controllers;

import com.example.backend.dto.NegotiationsDTO;
import com.example.backend.models.Users;
import com.example.backend.services.NegotiationsService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/negotiations")
public class NegotiationsController {
    private final NegotiationsService negotiationsService;

    public NegotiationsController(NegotiationsService negotiationsService){
        this.negotiationsService = negotiationsService;
    }

    @PostMapping("/{projectId}")
    public ResponseEntity<Void> addValue(
            @PathVariable UUID projectId,
            @RequestBody NegotiationsDTO dto,
            @AuthenticationPrincipal Users user
            ){
        negotiationsService.saveNegotiations(projectId, dto, user);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }


}
