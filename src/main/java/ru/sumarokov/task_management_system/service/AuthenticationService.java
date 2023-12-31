package ru.sumarokov.task_management_system.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.sumarokov.task_management_system.config.JwtService;
import ru.sumarokov.task_management_system.dto.TokenDto;
import ru.sumarokov.task_management_system.dto.UserInfoDto;
import ru.sumarokov.task_management_system.entity.User;
import ru.sumarokov.task_management_system.exception.EntityNotFoundException;
import ru.sumarokov.task_management_system.repository.UserRepository;

@Service
public class AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Autowired
    public AuthenticationService(UserRepository userRepository,
                                 PasswordEncoder passwordEncoder,
                                 JwtService jwtService,
                                 AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    public TokenDto register(UserInfoDto userInfoDto) {
        if (userRepository.existsByEmail(userInfoDto.getEmail())) {
            throw new IllegalArgumentException("User is already registered");
        }
        User user = new User();
        user.setEmail(userInfoDto.getEmail());
        user.setPassword(passwordEncoder.encode(userInfoDto.getPassword()));
        userRepository.save(user);
        String jwtToken = jwtService.generateToken(user);
        return new TokenDto(jwtToken);
    }

    public TokenDto authenticate(UserInfoDto userInfoDto) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(userInfoDto.getEmail(), userInfoDto.getPassword())
        );
        User user = userRepository.findByEmail(userInfoDto.getEmail())
                .orElseThrow(() -> new EntityNotFoundException(User.class, userInfoDto.getEmail()));
        String jwtToken = jwtService.generateToken(user);
        return new TokenDto(jwtToken);
    }
}
