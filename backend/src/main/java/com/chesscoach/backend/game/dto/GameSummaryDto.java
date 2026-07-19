package com.chesscoach.backend.game.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

// the response sent to frontend when a user views their games
public record GameSummaryDto(
        UUID gameId,
        String whitePlayer,
        String blackPlayer,
        String event,
        LocalDate gameDate,
        String result,
        LocalDateTime uploadedAt
){}
