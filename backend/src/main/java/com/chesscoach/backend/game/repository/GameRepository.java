package com.chesscoach.backend.game.repository;

import com.chesscoach.backend.game.entity.Game;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface GameRepository extends JpaRepository<Game, UUID> {
    List<Game> findByUserIdOrderByUploadedAtDesc(UUID userId);
    Optional<Game> findByIdAndUserId(UUID id,UUID userId);
}
