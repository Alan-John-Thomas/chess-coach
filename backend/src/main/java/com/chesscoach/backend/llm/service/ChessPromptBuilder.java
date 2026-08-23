package com.chesscoach.backend.llm.service;

import com.chesscoach.backend.analysis.dto.EvaluationResult;
import org.springframework.stereotype.Component;

@Component
public class ChessPromptBuilder {

    private static final String SYSTEM_CONTEXT = """
            You are an expert chess coach. You explain chess positions, tactics, and strategy 
            in clear, beginner-friendly language. You must strictly base your analysis on the 
            Stockfish engine evaluation provided below. Never invent moves or contradict 
            the engine output. Keep responses concise and instructional.
            """;

    public String buildPrompt(String fen, EvaluationResult evaluation, String userQuestion) {
        StringBuilder prompt = new StringBuilder();

        prompt.append(SYSTEM_CONTEXT).append("\n\n");

        // Position context
        prompt.append("--- POSITION DATA ---\n");
        prompt.append("FEN: ").append(fen).append("\n");

        // Engine evaluation context
        prompt.append("Stockfish Evaluation: ");
        if (evaluation.mateInMoves() != null) {
            int mate = evaluation.mateInMoves();
            if (mate > 0) {
                prompt.append("White has forced checkmate in ").append(mate).append(" moves.\n");
            } else {
                prompt.append("Black has forced checkmate in ").append(Math.abs(mate)).append(" moves.\n");
            }
        } else if (evaluation.centipawns() != null) {
            double eval = evaluation.centipawns();
            prompt.append(String.format("%+.2f", eval));
            if (Math.abs(eval) < 0.3) {
                prompt.append(" (roughly equal position)\n");
            } else if (eval > 0) {
                prompt.append(" (White is better)\n");
            } else {
                prompt.append(" (Black is better)\n");
            }
        }

        if (evaluation.bestMove() != null) {
            prompt.append("Engine Best Move: ").append(evaluation.bestMove()).append("\n");
        }

        if (evaluation.principalVariation() != null) {
            prompt.append("Engine Continuation Line: ").append(evaluation.principalVariation()).append("\n");
        }

        // User question
        prompt.append("\n--- USER QUESTION ---\n");
        prompt.append(userQuestion).append("\n");

        return prompt.toString();
    }
}