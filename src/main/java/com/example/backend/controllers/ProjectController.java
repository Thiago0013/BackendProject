package com.example.backend.controllers;

import com.example.backend.dto.ProjectDTO;
import com.example.backend.dto.ProjectResponseDTO;
import com.example.backend.dto.ProjectWithClientDTO;
import com.example.backend.models.Users;
import com.example.backend.services.ProjectService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/project")
public class ProjectController {

    private final ProjectService projectService;

    public ProjectController(ProjectService projectService){
        this.projectService = projectService;
    }

    @GetMapping
    public ResponseEntity<List<ProjectWithClientDTO>> getProject(){
        return ResponseEntity.ok(projectService.getAll());
    }

    @PostMapping
    public ResponseEntity<ProjectResponseDTO> create(@RequestBody ProjectDTO dto, Authentication authentication){
        Users user = (Users) authentication.getPrincipal();
        return ResponseEntity.ok(projectService.create(dto, user));
    }
}
