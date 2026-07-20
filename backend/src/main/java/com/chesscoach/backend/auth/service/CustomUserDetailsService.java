package com.chesscoach.backend.auth.service;

import com.chesscoach.backend.auth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

//Custom implementation of UserDetailsService to integrate with the local UserRepository
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    //connect to current db repo
    private final UserRepository userRepository;

    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException{
        return userRepository.findByEmail(email)
                .orElseThrow(()-> new UsernameNotFoundException("user not found with email: "+email));
    }
}
