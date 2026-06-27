package com.chesscoach.backend.auth;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name="users")
@Getter
@Setter
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name="id",updatable = false,nullable = false)
    private UUID id;
    @Column(unique = true,nullable = false)
    private String email;
    @Column(name="password_hash",nullable = false)
    private String passwordHash;
    @Column(name="username")
    private String userName;
    @Column(name="created_at",updatable = false)
    private LocalDateTime createdAt;
}
