package com.example.backend.services;

import com.example.backend.dto.LoginDTO;
import com.example.backend.dto.UsersDTO;
import com.example.backend.models.Cliente;
import com.example.backend.models.Providers;
import com.example.backend.models.Users;
import com.example.backend.models.enums.UserType;
import com.example.backend.repositories.ClienteRepository;
import com.example.backend.repositories.ProvidersRepository;
import com.example.backend.repositories.UsersRepository;
import jakarta.transaction.Transactional;
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
    private final ProvidersRepository providersRepo;
    private final ClienteRepository clientRepo;

    public AuthService(UsersRepository userRepo,
                       PasswordEncoder passwordEncoder,
                       @Lazy AuthenticationManager authenticationManager,
                       TokenService tokenService,
                       ProvidersRepository providersRepo,
                       ClienteRepository clientRepo){
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.tokenService = tokenService;
        this.providersRepo = providersRepo;
        this.clientRepo = clientRepo;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepo.findByEmail(username);
    }

    @Transactional
    public Users create(UsersDTO dto){
        if(userRepo.existsByEmail(dto.email())){
            throw new RuntimeException("ERRO: Este email já existe!");
        }
        Users newUser = new Users();
        newUser.setNome(dto.name());
        newUser.setEmail(dto.email());
        newUser.setPassword(passwordEncoder.encode(dto.password()));
        newUser.setPhone(dto.phone());

        Users savedUser = userRepo.save(newUser);

        if(dto.userType() == UserType.CLIENT){
            Cliente client = new Cliente();
            client.setUser(savedUser);

            clientRepo.save(client);
        } else {
            Providers providers = new Providers();
            providers.setUser(savedUser);

            providersRepo.save(providers);
        }

        return savedUser;
    }

    public String login(LoginDTO dto){
        var usernamePassword = new UsernamePasswordAuthenticationToken(dto.login(), dto.password());

        var auth = this.authenticationManager.authenticate(usernamePassword);

        return tokenService.generateToken((Users) Objects.requireNonNull(auth.getPrincipal()));
    }
}