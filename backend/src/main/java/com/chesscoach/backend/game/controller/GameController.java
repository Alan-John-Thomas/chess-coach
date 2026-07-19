package com.chesscoach.backend.game.controller;

import com.chesscoach.backend.auth.entity.User;
import com.chesscoach.backend.game.dto.GameDetailDto;
import com.chesscoach.backend.game.dto.GameSummaryDto;
import com.chesscoach.backend.game.dto.GameUploadResponse;
import com.chesscoach.backend.game.exception.InvalidPgnException;
import com.chesscoach.backend.game.service.GameService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/games")
@RequiredArgsConstructor
public class GameController {
    // required service
    private final GameService gameService;

    // upload endpoint
    @PostMapping("/upload")
    public ResponseEntity<GameUploadResponse> uploadGame(
            @RequestParam MultipartFile file,
            @AuthenticationPrincipal User user
            ){
        // check if file empty
        if (file.isEmpty()) {
            throw new InvalidPgnException("Cannot upload empty file");
        }

        GameUploadResponse gameUploadResponse=gameService.uploadGame(file,user);
        return ResponseEntity.status(HttpStatus.CREATED).body(gameUploadResponse);
    }

    // get endpoint to fetch all games of current user
    @GetMapping
    public ResponseEntity<List<GameSummaryDto>> getUserGames(
            @AuthenticationPrincipal User user
    ){
        List<GameSummaryDto> gameSummaryDto = gameService.getUserGames(user.getId());
        return ResponseEntity.ok(gameSummaryDto);
    }

    // get endpoint to fetch game details of a specific gameId
    @GetMapping("/{id}")
    public  ResponseEntity<GameDetailDto> getGameDetail(
            @PathVariable UUID id,
            @AuthenticationPrincipal User user
    ){
        GameDetailDto gameDetailDto = gameService.getGameDetail(id,user.getId());
        return ResponseEntity.ok(gameDetailDto);
    }
}
