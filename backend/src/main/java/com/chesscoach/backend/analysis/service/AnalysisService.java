package com.chesscoach.backend.analysis.service;

import com.chesscoach.backend.analysis.dto.EvaluationResult;
import com.chesscoach.backend.analysis.engine.StockfishClient;
import com.chesscoach.backend.analysis.entity.PositionCache;
import com.chesscoach.backend.analysis.repository.PositionCacheRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class AnalysisService {
    private final PositionCacheRepository positionCacheRepository;
    private final StockfishClient stockfishClient;

    public EvaluationResult analysePosition(String fen, int depth) {

        // check the database cache for analysis
        Optional<PositionCache> cached = positionCacheRepository.findById(fen);
        if (cached.isPresent()) {
            log.info("Cache HIT for FEN: {}", fen);
            PositionCache cache = cached.get(); // get() captures the optional value since its present.
            return new EvaluationResult(cache.getEvaluation(), cache.getMateInMoves(), cache.getBestMove(),cache.getTopLines());
        }

        // else use the stockfish
        log.info("Cache MISS for FEN: {}. Analyzing with Stockfish at depth {}", fen, depth);
        EvaluationResult result = stockfishClient.evaluatePosition(fen,depth);

        // add data as cache to database
        PositionCache cache = PositionCache.builder().fen(fen).evaluation(result.centipawns()).mateInMoves(result.mateInMoves()).bestMove(result.bestMove()).topLines(result.principalVariation()).build();
        positionCacheRepository.save(cache);

        return result;
    }
}
