package com.chesscoach.backend.game.dto;

import java.util.UUID;

//the response sent to frontend after uploading a game
public record GameUploadResponse(
        UUID gameId,
        String whitePlayer,
        String blackPlayer,
        String result,
        int totalMoves,
        String message
){}
