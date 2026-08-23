package com.chesscoach.backend.llm.dto;

public record CoachExplanationDto(
        String fen,
        Double evaluation,
        Integer mateInMoves,
        String bestMove,
        String principalVariation,
        String explanation
) {}