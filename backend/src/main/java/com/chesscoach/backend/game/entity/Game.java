package com.chesscoach.backend.game.entity;

import com.chesscoach.backend.auth.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name="games")
@Getter
@Setter
public class Game {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @ManyToOne(fetch= FetchType.LAZY) // joins only when needed
    @JoinColumn(name="user_id",nullable = false) //joins the user_id in games with primary key of user
    private User user;
    @Column(nullable=false,columnDefinition = "TEXT")
    private String pgn;
    private String whitePlayer;
    private String blackPlayer;
    private String event;
    private LocalDate gameDate;
    private String result;
    @CreationTimestamp
    private LocalDateTime uploadedAt;
}
