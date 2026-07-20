package com.chesscoach.backend.game.exception;

// inside game folder as its part of the game logic
public class GameNotFoundException extends RuntimeException {
    public GameNotFoundException(String message) {
        super(message);
    }
}
