package com.chesscoach.backend.config;

import com.chesscoach.backend.auth.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {

        // get the auth header from the http request
        final String authHeader = request.getHeader("Authorization");

        // skip filter if request doesn't have a header
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            // end the filter and pass to next filter
            filterChain.doFilter(request, response);
            return;
        }

        // extract token and email
        final String jwt = authHeader.substring(7);
        final String userEmail = jwtService.extractUsername(jwt);

        //verify if the user was authenticated before in the current security context
        if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(userEmail);

            //validate the token and create the auth token object
            if (jwtService.isValidToken(jwt, userDetails)) {

                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities()
                );

                // Attach details regarding the incoming request (e.g., IP address, session ID)
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // update the context with current authentication
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }

        }

        // continue with next filter execution if any
        filterChain.doFilter(request,response);

    }
}
