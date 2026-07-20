package com.chesscoach.backend.game.repository;

import com.chesscoach.backend.game.entity.Game;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface GameRepository extends JpaRepository<Game, UUID> {
    // Retrieves all games belonging to the specified user, ordered by upload time in descending order (newest first)
    List<Game> findByUserIdOrderByUploadedAtDesc(UUID userId);
    // Retrieves a game only if both the game ID and the owner's user ID match.
    // Used to ensure users can access only their own games
    Optional<Game> findByIdAndUserId(UUID id,UUID userId);
}
