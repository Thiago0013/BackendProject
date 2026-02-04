package com.example.backend.controllers;

import com.example.backend.dto.LoginDTO;
import com.example.backend.dto.UsersDTO;
import com.example.backend.models.Users;
import com.example.backend.services.AuthService;
import org.apache.catalina.User;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService){
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<Users> register(@RequestBody UsersDTO dto){
        Users user = authService.create(dto);
        return ResponseEntity.ok(user);
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginDTO dto){
        var token = authService.login(dto);
        return ResponseEntity.ok(token);
    }
}
