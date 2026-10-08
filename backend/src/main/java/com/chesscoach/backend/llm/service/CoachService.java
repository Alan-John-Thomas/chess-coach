package com.chesscoach.backend.llm.service;

import com.chesscoach.backend.analysis.dto.EvaluationResult;
import com.chesscoach.backend.analysis.service.AnalysisService;
import com.chesscoach.backend.llm.dto.CoachExplanationDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class CoachService {
    private final AnalysisService analysisService;
    private final ChessPromptBuilder chessPromptBuilder;
    private final LlmService llmService;

    public CoachExplanationDto explainPosition(String fen,int depth,String userQuestion){
        log.info("Requesting coach explanation for FEN: {}", fen);
        // Get engine analysis (either from PostgreSQL cache or Stockfish)
        EvaluationResult evaluation = analysisService.analysePosition(fen, depth);
        // Set default question if user didn't specify one
        String question = (userQuestion != null && !userQuestion.isBlank())
                ? userQuestion
                : "Explain the best plan and key tactical idea in this position.";
        // Build grounded prompt
        // the prompt building doesn't make use of chat history (as buildPrompt fn is the one called)
        // this is for quick analysis without any context
        String prompt = chessPromptBuilder.buildPrompt(fen, evaluation, question);
        // Generate LLM explanation
        String explanation = llmService.generateResponse(prompt);
        // Return complete structured response
        return new CoachExplanationDto(
                fen,
                evaluation.centipawns(),
                evaluation.mateInMoves(),
                evaluation.bestMove(),
                evaluation.principalVariation(),
                explanation
        );
    }
}
