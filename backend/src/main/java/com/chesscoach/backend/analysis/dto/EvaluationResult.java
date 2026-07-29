package com.chesscoach.backend.analysis.dto;

public record EvaluationResult(
   Double centipawns,
   Integer mateInMoves,
   String bestMove
) {}
