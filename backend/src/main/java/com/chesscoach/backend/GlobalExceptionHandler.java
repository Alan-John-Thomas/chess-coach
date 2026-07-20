package com.chesscoach.backend;

import com.chesscoach.backend.game.exception.GameNotFoundException;
import com.chesscoach.backend.game.exception.InvalidPgnException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import java.util.HashMap;
import java.util.Map;

// global exception handles exceptions to the types defined below
// no need to handle these exceptions separately whenever thrown
@RestControllerAdvice
public class GlobalExceptionHandler {
    // exception logic for GameNotFoundException
    @ExceptionHandler(GameNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleGameNotFound(GameNotFoundException ex) {
        Map<String, String> error = new HashMap<>();
        error.put("error", "Not Found");
        error.put("message", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }
    // exception logic for InvalidPgnException
    @ExceptionHandler(InvalidPgnException.class)
    public ResponseEntity<Map<String, String>> handleInvalidPgn(InvalidPgnException ex) {
        Map<String, String> error = new HashMap<>();
        error.put("error", "Bad Request");
        error.put("message", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }
    // exception logic for MaxUploadSizeExceededException
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<Map<String, String>> handleMaxSizeException(MaxUploadSizeExceededException ex) {
        Map<String, String> error = new HashMap<>();
        error.put("error", "Payload Too Large");
        error.put("message", "The uploaded file is too large. Maximum size is 1MB.");
        return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE).body(error);
    }
}
