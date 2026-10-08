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
import com.chesscoach.backend.game.repository.GameRepository;
import com.chesscoach.backend.llm.service.ChessPromptBuilder;
import com.chesscoach.backend.llm.service.LlmService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ChatServiceTest {

    @Mock
    private ChatSessionRepository chatSessionRepository;

    @Mock
    private ChatMessageRepository chatMessageRepository;

    @Mock
    private GameRepository gameRepository;

    @Mock
    private AnalysisService analysisService;

    @Mock
    private ChessPromptBuilder chessPromptBuilder;

    @Mock
    private LlmService llmService;

    @Mock
    private BoardVariationService boardVariationService;

    @InjectMocks
    private ChatService chatService;

    private User owner;
    private ChatSession session;
    private UUID sessionId;

    @BeforeEach
    void setUp() {
        sessionId = UUID.randomUUID();
        owner = new User();
        owner.setId(UUID.randomUUID());
        owner.setEmail("magnus@chess.com");

        session = ChatSession.builder()
                .id(sessionId)
                .user(owner)
                .build();
    }

    // Authorized User + Board FEN present
    @Test
    void sendMessage_HappyPath_ReturnsAssistantMessage() {
        String fen = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";
        SendMessageRequest request = new SendMessageRequest("Why is e4 good?", fen);
        EvaluationResult evalResult = new EvaluationResult(0.30, null, "e2e4", "e2e4 e7e5");

        when(chatSessionRepository.findById(sessionId)).thenReturn(Optional.of(session));
        when(analysisService.analysePosition(fen, 18)).thenReturn(evalResult);
        when(chessPromptBuilder.buildChatPrompt(eq(fen), eq(evalResult), any(), eq("Why is e4 good?"))).thenReturn("prompt");
        when(llmService.generateResponse("prompt")).thenReturn("e4 controls the center.");
        when(boardVariationService.formatToJson(any(), eq(8))).thenReturn("[\"e2e4\",\"e7e5\"]");
        when(chatMessageRepository.save(any(ChatMessage.class))).thenAnswer(invocation -> invocation.getArgument(0));

        ChatMessageDto response = chatService.sendMessage(sessionId, owner, request);

        assertNotNull(response);
        assertEquals(MessageRole.ASSISTANT, response.role());
        assertEquals("e4 controls the center.", response.content());
        assertEquals("[\"e2e4\",\"e7e5\"]", response.boardVariation());

        // Verify collaborating methods were called
        verify(analysisService, times(1)).analysePosition(fen, 18);
        verify(llmService, times(1)).generateResponse("prompt");
        verify(chatMessageRepository, times(2)).save(any(ChatMessage.class)); // 1 User + 1 Assistant
    }

    // SECURITY TEST (Unauthorized User)
    @Test
    void sendMessage_UnauthorizedUser_ThrowsExceptionAndNeverCallsLlm() {
        User imposter = new User();
        imposter.setId(UUID.randomUUID()); // Different user ID!

        SendMessageRequest request = new SendMessageRequest("Let me spy on this chat", null);
        when(chatSessionRepository.findById(sessionId)).thenReturn(Optional.of(session));

        // Assert that the unauthorized access blows up
        assertThrows(RuntimeException.class, () -> {
            chatService.sendMessage(sessionId, imposter, request);
        });

        // Ensure LLM and Stockfish were NEVER touched
        verify(llmService, never()).generateResponse(any());
        verify(analysisService, never()).analysePosition(any(), anyInt());
        verify(chatMessageRepository, never()).save(any());
    }

    // EDGE CASE (General Question without Board FEN)
    @Test
    void sendMessage_WithoutFen_SkipsStockfishEvaluation() {
        // null FEN represents a general question like "How does castling work?"
        SendMessageRequest request = new SendMessageRequest("How does castling work?", null);

        when(chatSessionRepository.findById(sessionId)).thenReturn(Optional.of(session));
        when(chessPromptBuilder.buildChatPrompt(isNull(), isNull(), any(), eq("How does castling work?"))).thenReturn("prompt");
        when(llmService.generateResponse("prompt")).thenReturn("Castling protects your king.");
        when(chatMessageRepository.save(any(ChatMessage.class))).thenAnswer(invocation -> invocation.getArgument(0));

        ChatMessageDto response = chatService.sendMessage(sessionId, owner, request);

        assertNotNull(response);
        assertEquals("Castling protects your king.", response.content());

        // CRUCIAL: Verify Stockfish was SKIPPED to save CPU
        verify(analysisService, never()).analysePosition(any(), anyInt());
        // Verify LLM WAS still called
        verify(llmService, times(1)).generateResponse("prompt");
    }
}