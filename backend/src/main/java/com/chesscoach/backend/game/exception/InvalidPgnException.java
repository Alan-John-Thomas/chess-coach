package com.chesscoach.backend.game.exception;

// inside game folder as its part of the game logic
public class InvalidPgnException extends RuntimeException {
    public InvalidPgnException(String message) {
        super(message);
    }
}
