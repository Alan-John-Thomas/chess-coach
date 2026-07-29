package com.chesscoach.backend.analysis.engine;

import com.chesscoach.backend.analysis.dto.EvaluationResult;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("dev")
class StockfishClientTest {

    @Autowired
    private StockfishClient stockfishClient;

    // test the stockfish client
    @Test
    void evaluatePosition_StartingPosition_ReturnsValidEvaluation() {
        String startFen = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";

        EvaluationResult result = stockfishClient.evaluatePosition(startFen, 10);

        // test fails if assertion is NULL
        assertNotNull(result);
        assertNotNull(result.bestMove());
        assertNotNull(result.centipawns());

        System.out.println("Starting position best move: " + result.bestMove() + ", eval: " + result.centipawns());
    }
}