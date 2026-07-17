package com.chesscoach.backend.game.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

// the response sent to frontend when a user views the details of a specific game
// contains full game detail with moves and FENs
public record GameDetailDto(
    UUID gameId,
    String whitePlayer,
    String blackPlayer,
    String event,
    LocalDate gameDate,
    String result,
    LocalDateTime uploadedAt,
    String pgn,
    List<String> moves,
    List<String> fens
){}
