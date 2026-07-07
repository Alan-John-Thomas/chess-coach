package com.chesscoach.backend.auth.service;

import com.chesscoach.backend.auth.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;

@Service
public class JwtService {

    @Value("${security.jwt.secret-key}")
    private String secretKey;

    @Value("${security.jwt.expiration-time}")
    private long jwtExpiration;

    // 1. Generate the token using the User's email
    public String generateToken(User user) {
        return Jwts.builder()
                .setSubject(user.getEmail())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpiration))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    // 2. Decode the secret key string into a cryptographic Key object as secret key is just a text,and we need to convert it.
    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    //3. extract username(email for our case) from the token
    public String extractUsername(String token){
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)  //verification (checks if JWT is valid , non expired),throws error otherwise
                .getBody();
        return claims.getSubject();
    }

    //4. check if a token belongs to the right requesting user
    public boolean isValidToken(String token , UserDetails userDetails){
        final String userName = extractUsername(token);
        // Is the email matching
        return (userName.equals(userDetails.getUsername()));
    }
}