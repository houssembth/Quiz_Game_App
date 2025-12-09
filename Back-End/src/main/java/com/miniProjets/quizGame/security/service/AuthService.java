package com.miniProjets.quizGame.security.service;

import com.miniProjets.quizGame.model.User;
import com.miniProjets.quizGame.repository.UserRepository;
import com.miniProjets.quizGame.security.model.AuthRequest;
import com.miniProjets.quizGame.security.model.AuthResponse;
import com.miniProjets.quizGame.security.model.RegisterRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private CustomUserDetailsService userDetailsService;

    public AuthResponse register(RegisterRequest request) {
        // Validate role
        if (request.getRole() == null || request.getRole().isEmpty()) {
            request.setRole("PLAYER"); // Default role
        }

        if (!request.getRole().equals("PLAYER") && !request.getRole().equals("ADMIN")) {
            throw new RuntimeException("Invalid role. Must be PLAYER or ADMIN");
        }

        // Check if user already exists
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        if (userRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("Username already exists");
        }

        // Create new user
        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(request.getRole());

        userRepository.save(user);

        // Generate JWT token
        var userDetails = userDetailsService.loadUserByUsername(user.getEmail());
        var jwtToken = jwtService.generateToken(userDetails);

        return new AuthResponse(
                jwtToken,
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getRole()
        );
    }

    public AuthResponse login(AuthRequest request) {
        // Authenticate user
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        // Get user from database
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Generate JWT token
        var userDetails = userDetailsService.loadUserByUsername(user.getEmail());
        var jwtToken = jwtService.generateToken(userDetails);

        return new AuthResponse(
                jwtToken,
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getRole()
        );
    }
}
