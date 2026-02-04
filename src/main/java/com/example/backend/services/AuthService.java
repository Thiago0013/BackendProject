package com.example.backend.services;

import com.example.backend.dto.UsersDTO;
import com.example.backend.models.Users;
import com.example.backend.repositories.UsersRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService implements UserDetailsService {

    private final UsersRepository userRepo;
    private final PasswordEncoder passwordEncoder; // ← injetar aqui

    public AuthService(UsersRepository userRepo, PasswordEncoder passwordEncoder){
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepo.findByEmail(username);
    }

    public Users create(UsersDTO dto){
        Users newUser = new Users();
        newUser.setNome(dto.name());
        newUser.setEmail(dto.email());
        newUser.setPassword(passwordEncoder.encode(dto.password()));
        newUser.setPhone(dto.phone());

        return userRepo.save(newUser);
    }
}