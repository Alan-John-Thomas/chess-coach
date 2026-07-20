package com.chesscoach.backend.auth.service;

import com.chesscoach.backend.auth.dto.AuthResponse;
import com.chesscoach.backend.auth.dto.LoginRequest;
import com.chesscoach.backend.auth.dto.RegisterRequest;
import com.chesscoach.backend.auth.entity.User;
import com.chesscoach.backend.auth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    //this will call the DaoAuthProvider
    private final AuthenticationManager authenticationManager;

    // registration logic
    public User register(RegisterRequest request) {

        // check if email taken
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("Email already registered");
        }

        // create a user obj with same values as request
        User user = new User();
        user.setUserName(request.getUsername());
        user.setEmail(request.getEmail());
        user.setCreatedAt(java.time.LocalDateTime.now());

        // encrypt password and save to database
        String hashedPassword = passwordEncoder.encode(request.getPassword());
        user.setPasswordHash(hashedPassword);
        userRepository.save(user);
        // return
        return user;
    }

    //login logic (login endpoint return a Jwt token)
    public AuthResponse login(LoginRequest request) {

        // 1. Hand the credentials to the Manager (which hands it to our DAO!)
        // If the password is wrong, this line throws an exception and stops execution.
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );
        // 2. If we reach this line, the DAO proved the user is legit!
        // We just fetch them so we can put their details in the JWT.
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        // 3. Generate JWT token and return it
        String jwtToken = jwtService.generateToken(user);
        return new AuthResponse(jwtToken);
    }
}
