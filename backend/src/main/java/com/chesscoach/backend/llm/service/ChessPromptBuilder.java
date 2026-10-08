package com.chesscoach.backend.llm.service;

import com.chesscoach.backend.analysis.dto.EvaluationResult;
import com.chesscoach.backend.chat.entity.ChatMessage;
import com.chesscoach.backend.chat.entity.MessageRole;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ChessPromptBuilder {

    private static final String SYSTEM_CONTEXT = """
            You are an expert chess coach. You explain chess positions, tactics, and strategy 
            in clear, beginner-friendly language. You must strictly base your analysis on the 
            Stockfish engine evaluation provided below. Never invent moves or contradict 
            the engine output. Keep responses concise and instructional.
            """;

    // Build the prompt without including chat history
    public String buildPrompt(String fen, EvaluationResult evaluation, String userQuestion){
        return buildChatPrompt(fen, evaluation, List.of(), userQuestion);
    }

    // Build the prompt including message History
    public String buildChatPrompt(String fen,
                                  EvaluationResult evaluation,
                                  List<ChatMessage> history,
                                  String userQuestion
    ) {
        StringBuilder prompt = new StringBuilder();

        prompt.append(SYSTEM_CONTEXT).append("\n\n");

        // Append conversation history if present
        if (history != null && !history.isEmpty()) {
            prompt.append("--- CONVERSATION HISTORY ---\n");
            for (ChatMessage msg : history) {
                String sender = msg.getRole() == MessageRole.USER ? "User" : "Coach";
                prompt.append(sender).append(": ").append(msg.getContent()).append("\n");
            }
            prompt.append("\n");
        }

        // Position context
        if (fen != null && !fen.isBlank()) {
            prompt.append("--- CURRENT POSITION DATA ---\n");
            prompt.append("FEN: ").append(fen).append("\n");
            if (evaluation != null) {
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
                    //.format() here formats float to two point precision with +,- added before the number
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
            }
            prompt.append("\n");
        }
        // New user question
        prompt.append("--- NEW USER QUESTION ---\n");
        prompt.append(userQuestion).append("\n");
        return prompt.toString();
    }
}