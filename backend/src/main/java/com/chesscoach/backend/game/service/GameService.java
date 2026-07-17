package com.chesscoach.backend.game.service;

import com.chesscoach.backend.auth.entity.User;
import com.chesscoach.backend.game.dto.GameDetailDto;
import com.chesscoach.backend.game.dto.GameSummaryDto;
import com.chesscoach.backend.game.dto.GameUploadResponse;
import com.chesscoach.backend.game.dto.ParsedGame;
import com.chesscoach.backend.game.entity.Game;
import com.chesscoach.backend.game.repository.GameRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GameService {
    // required services
    private final PgnParserService pgnParserService;
    private final GameRepository gameRepository;

    // function to parse the uploaded pgn and save to database
    public GameUploadResponse uploadGame(MultipartFile file, User user){
        try{
            String rawPgn = new String(file.getBytes(), StandardCharsets.UTF_8);
            ParsedGame parsedGame = pgnParserService.parsePgn(rawPgn);
            // create a game object and add the parsed details
            Game game = new Game();
            game.setUser(user);
            game.setPgn(rawPgn);
            game.setWhitePlayer(parsedGame.whitePlayer());
            game.setBlackPlayer(parsedGame.blackPlayer());
            game.setEvent(parsedGame.event());
            game.setGameDate(parsedGame.gameDate());
            game.setResult(parsedGame.result());

            Game saved = gameRepository.save(game);

            return new GameUploadResponse(
                    saved.getId(),
                    saved.getWhitePlayer(),
                    saved.getBlackPlayer(),
                    saved.getResult(),
                    parsedGame.moves().size(),
                    "Game uploaded successfully!"
            );
        }
        catch (IOException e){
            throw new RuntimeException("Failed to read uploaded file: " + e.getMessage());
        }
    }

    // function to get games belonging to a user
    public List<GameSummaryDto> getUserGames(UUID userId){
        return gameRepository.findByUserIdOrderByUploadedAtDesc(userId)
                .stream().map(game->new GameSummaryDto(
                        game.getId(),
                        game.getWhitePlayer(),
                        game.getBlackPlayer(),
                        game.getEvent(),
                        game.getGameDate(),
                        game.getResult(),
                        game.getUploadedAt()

                )).toList();
    }

    // function to get details of a specific game (PGNs,FENs included)
    public GameDetailDto getGameDetail(UUID gameId, UUID userId){
        Game game = gameRepository.findByIdAndUserId(gameId,userId)
                .orElseThrow(() -> new RuntimeException("Game not found"));;

        ParsedGame parsed = pgnParserService.parsePgn(game.getPgn());

        return new GameDetailDto(
                game.getId(),
                game.getWhitePlayer(),
                game.getBlackPlayer(),
                game.getEvent(),
                game.getGameDate(),
                game.getResult(),
                game.getUploadedAt(),
                game.getPgn(),
                parsed.moves(),
                parsed.fens()
        );
    }
}
