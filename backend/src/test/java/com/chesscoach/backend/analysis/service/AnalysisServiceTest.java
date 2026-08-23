package com.chesscoach.backend.analysis.service;

import com.chesscoach.backend.analysis.dto.EvaluationResult;
import com.chesscoach.backend.analysis.engine.StockfishClient;
import com.chesscoach.backend.analysis.entity.PositionCache;
import com.chesscoach.backend.analysis.repository.PositionCacheRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AnalysisServiceTest {

    @Mock
    private StockfishClient stockfishClient;

    @Mock
    private PositionCacheRepository positionCacheRepository;

    @InjectMocks
    private AnalysisService analysisService;

    @Test
    void analyzePosition_CacheHit_ReturnsCachedValueWithoutCallingStockfish() {
        String fen = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";
        PositionCache cached = PositionCache.builder()
                .fen(fen)
                .evaluation(0.25)
                .bestMove("e2e4")
                .build();

        when(positionCacheRepository.findById(fen)).thenReturn(Optional.of(cached));

        EvaluationResult result = analysisService.analysePosition(fen, 18);

        assertNotNull(result);
        assertEquals(0.25, result.centipawns());
        assertEquals("e2e4", result.bestMove());

        // Verify Stockfish was NEVER invoked
        verify(stockfishClient, never()).evaluatePosition(anyString(), anyInt());
        verify(positionCacheRepository, never()).save(any());
    }

    @Test
    void analyzePosition_CacheMiss_CallsStockfishAndSavesToCache() {
        String fen = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";
        EvaluationResult engineResult = new EvaluationResult(0.35, null, "e2e4", "e2e4 e7e5");

        when(positionCacheRepository.findById(fen)).thenReturn(Optional.empty());
        when(stockfishClient.evaluatePosition(fen, 18)).thenReturn(engineResult);

        EvaluationResult result = analysisService.analysePosition(fen, 18);

        assertNotNull(result);
        assertEquals(0.35, result.centipawns());
        assertEquals("e2e4", result.bestMove());

        // Verify Stockfish WAS called and result WAS cached
        verify(stockfishClient, times(1)).evaluatePosition(fen, 18);
        verify(positionCacheRepository, times(1)).save(any(PositionCache.class));
    }
}