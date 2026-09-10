package com.chesscoach.backend.chat.service;

import com.chesscoach.backend.analysis.dto.EvaluationResult;
import com.chesscoach.backend.analysis.service.AnalysisService;
import com.chesscoach.backend.auth.entity.User;
import com.chesscoach.backend.chat.dto.ChatMessageDto;
import com.chesscoach.backend.chat.dto.SendMessageRequest;
import com.chesscoach.backend.chat.entity.ChatMessage;
import com.chesscoach.backend.chat.entity.ChatSession;
import com.chesscoach.backend.chat.entity.MessageRole;
import com.chesscoach.backend.chat.repository.ChatMessageRepository;
import com.chesscoach.backend.chat.repository.ChatSessionRepository;
import com.chesscoach.backend.game.entity.Game;
import com.chesscoach.backend.game.repository.GameRepository;
import com.chesscoach.backend.llm.service.ChessPromptBuilder;
import com.chesscoach.backend.llm.service.LlmService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class ChatService {
    private final ChatSessionRepository chatSessionRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final GameRepository gameRepository;
    private final AnalysisService analysisService;
    private final ChessPromptBuilder chessPromptBuilder;
    private final LlmService llmService;

    // Get or create the unique chat session for a game
    @Transactional
    public ChatSession getOrCreateSession(UUID gameId, User user){
        return chatSessionRepository.findByGameIdAndUserId(gameId,user.getId())
                .orElseGet(()->{
                    Game game= gameRepository.findById(gameId)
                            .orElseThrow(()->new RuntimeException("Game not found with ID: " + gameId));

                    ChatSession newSession = ChatSession.builder()
                            .game(game)
                            .user(user)
                            .build();

                    return chatSessionRepository.save(newSession);
                    });
    }

    // Fetch full chronological chat history for a session
    public List<ChatMessageDto> getSessionMessages(UUID sessionId){
        return chatMessageRepository.findBySessionIdOrderByCreatedAtAsc(sessionId)
                .stream()
                .map(ChatMessageDto::fromEntity)
                .toList();
    }

    // Send message, get AI response with memory, and save both
    @Transactional
    public ChatMessageDto sendMessage(UUID sessionId, User user, SendMessageRequest request) {
        ChatSession session = chatSessionRepository.findById(sessionId)
                .orElseThrow(() -> new RuntimeException("Chat session not found with ID: " + sessionId));
        // Security check: ensure user owns this session
        if (!session.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Unauthorized access to chat session");
        }
        // 1. Save User Message
        ChatMessage userMessage = ChatMessage.builder()
                .session(session)
                .role(MessageRole.USER)
                .content(request.content())
                .moveContextFen(request.moveContextFen())
                .build();
        chatMessageRepository.save(userMessage);
        // 2. Fetch engine evaluation if a board position (FEN) is attached
        EvaluationResult evaluation = null;
        if (request.moveContextFen() != null && !request.moveContextFen().isBlank()) {
            evaluation = analysisService.analysePosition(request.moveContextFen(), 18);
        }
        // 3. Load past conversation history so AI has memory
        List<ChatMessage> history = chatMessageRepository.findBySessionIdOrderByCreatedAtAsc(sessionId);
        // 4. Build prompt with history included
        String prompt = chessPromptBuilder.buildChatPrompt(
                request.moveContextFen(),
                evaluation,
                history,
                request.content()
        );
        // 5. Generate response from DeepSeek / Ollama
        String responseContent = llmService.generateResponse(prompt);
        // 6. Save Assistant Message
        ChatMessage assistantMessage = ChatMessage.builder()
                .session(session)
                .role(MessageRole.ASSISTANT)
                .content(responseContent)
                .moveContextFen(request.moveContextFen())
                .boardVariation(evaluation != null ? evaluation.principalVariation() : null)
                .build();
        ChatMessage savedAssistantMsg = chatMessageRepository.save(assistantMessage);
        // 7. Return to frontend as DTO
        return ChatMessageDto.fromEntity(savedAssistantMsg);
    }
}
