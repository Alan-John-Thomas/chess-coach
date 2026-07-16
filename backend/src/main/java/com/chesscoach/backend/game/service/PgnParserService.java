package com.chesscoach.backend.game.service;

import com.chesscoach.backend.game.dto.ParsedGame;
import com.github.bhlangonijr.chesslib.Board;
import com.github.bhlangonijr.chesslib.game.Game;
import com.github.bhlangonijr.chesslib.move.Move;
import com.github.bhlangonijr.chesslib.move.MoveList;
import com.github.bhlangonijr.chesslib.pgn.PgnIterator;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class PgnParserService {
    public ParsedGame parsePgn(String rawPgn){
        // Split the raw PGN string into lines and feed it directly to PgnIterator
        // inside try() , as it will auto close and hence prevent memory leak
        try (PgnIterator pgnIterator = new PgnIterator(Arrays.asList(rawPgn.split("\n")));){
            // 1. Load the raw PGN string into the chesslib Game engine
            Game game = pgnIterator.iterator().next();
            game.loadMoveText(); // Parses the move text into actual chess moves
            // 2. Extract metadata
            String whitePlayer = game.getWhitePlayer().getName();
            String blackPlayer = game.getBlackPlayer().getName();
            String event = game.getRound().getEvent().getName();

            // Format the date (PGNs usually use yyyy.MM.dd)
            String dateStr = game.getDate();
            LocalDate gameDate = null;
            if (dateStr != null && !dateStr.contains("?")) {
                dateStr = dateStr.replace(".", "-");
                gameDate = LocalDate.parse(dateStr, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            }
            String result = game.getResult().value();
            // 3. Step through the game to collect Moves and FENs
            MoveList moveList = game.getHalfMoves();
            List<String> moves = new ArrayList<>();
            List<String> fens = new ArrayList<>();

            // create a virtual board to simulate the game step-by-step
            Board board = new Board();
            fens.add(board.getFen()); // Add the starting position
            for (Move move : moveList) {
                moves.add(move.toString()); // e.g. "e2e4"
                board.doMove(move); // Play the move on the virtual board
                fens.add(board.getFen()); // Take a snapshot of the new position
            }
            // 4. Build and return our immutable record!
            return new ParsedGame(
                    whitePlayer,
                    blackPlayer,
                    event,
                    gameDate,
                    result,
                    moves,
                    fens
            );

        } catch (Exception e) {
            throw new RuntimeException("Failed to parse PGN file: " + e.getMessage());
        }
    }
}
