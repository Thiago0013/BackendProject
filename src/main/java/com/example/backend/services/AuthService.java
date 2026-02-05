package com.example.backend.services;

import com.example.backend.dto.LoginDTO;
import com.example.backend.dto.UsersDTO;
import com.example.backend.models.Users;
import com.example.backend.repositories.UsersRepository;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class AuthService implements UserDetailsService {

    private final UsersRepository userRepo;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;

    public AuthService(UsersRepository userRepo,
                       PasswordEncoder passwordEncoder,
                       @Lazy AuthenticationManager authenticationManager,
                       TokenService tokenService){
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.tokenService = tokenService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepo.findByEmail(username);
    }

    public Users create(UsersDTO dto){
        if(userRepo.existsByEmail(dto.email())){
            throw new RuntimeException("ERRO: Este email já existe!");
        }
        Users newUser = new Users();
        newUser.setNome(dto.name());
        newUser.setEmail(dto.email());
        newUser.setPassword(passwordEncoder.encode(dto.password()));
        newUser.setPhone(dto.phone());

        return userRepo.save(newUser);
    }

    public String login(LoginDTO dto){
        var usernamePassword = new UsernamePasswordAuthenticationToken(dto.login(), dto.password());

        var auth = this.authenticationManager.authenticate(usernamePassword);

        return tokenService.generateToken((Users) Objects.requireNonNull(auth.getPrincipal()));
    }
}