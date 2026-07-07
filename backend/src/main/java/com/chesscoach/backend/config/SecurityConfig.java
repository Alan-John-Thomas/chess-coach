package com.chesscoach.backend.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration // means this class contains methods that create beans
@EnableWebSecurity // enables spring security for this application
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthFilter;
    private final UserDetailsService userDetailsService;

    // the auth provider is the class that verify login using the database when the user logs in initially without a JWT token.
    // that is auth provider will validate if username and password sent by user is valid (no need to manually write the logic).
    // authManager will call this AuthProvider
    @Bean
    public AuthenticationProvider authenticationProvider(){
        // pass in userDetailsService as authProvider authenticates by querying the database
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider(userDetailsService);
        // add the details the authentication provider will need
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    // authManager will call authProvider which is set to be the DaoAuthProvider
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                // Disable CSRF protection.
                .csrf(AbstractHttpConfigurer::disable)

                // Define authorization rules for incoming HTTP requests.
                .authorizeHttpRequests(auth -> auth

                        // Any endpoint starting with /api/auth/
                        // (e.g. /login, /register) can be accessed without a JWT.
                        .requestMatchers("/api/auth/**").permitAll()

                        // Every other endpoint requires the user to be authenticated.
                        .anyRequest().authenticated()
                )

                // config to NOT create HTTP sessions.
                // Every request must carry its own JWT for authentication.
                .sessionManagement(sess ->
                        sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )

                // Register the AuthenticationProvider responsible for
                // verifying email/password during initial login.
                .authenticationProvider(authenticationProvider())

                // Insert our custom JWT filter before Spring's built-in
                // UsernamePasswordAuthenticationFilter so JWT validation
                // happens first for every request.
                .addFilterBefore(
                        jwtAuthFilter,
                        UsernamePasswordAuthenticationFilter.class
                );

        // Build and return the configured SecurityFilterChain bean.
        return http.build();
    }
}
