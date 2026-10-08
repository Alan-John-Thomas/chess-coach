package com.chesscoach.backend.chat.controller;

import com.chesscoach.backend.auth.entity.User;
import com.chesscoach.backend.chat.dto.ChatMessageDto;
import com.chesscoach.backend.chat.dto.ChatSessionDto;
import com.chesscoach.backend.chat.dto.SendMessageRequest;
import com.chesscoach.backend.chat.entity.ChatSession;
import com.chesscoach.backend.chat.service.ChatService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/chat")
@RequiredArgsConstructor
public class ChatController {
    private final ChatService chatService;

    // gets the session or creates a new session
    @PostMapping("/sessions")
    public ResponseEntity<ChatSessionDto> getSession(
            @RequestParam UUID gameId,
            @AuthenticationPrincipal User user
    ){
        ChatSession session = chatService.getOrCreateSession(gameId,user);
        return ResponseEntity.ok(ChatSessionDto.fromEntity(session)); // returns ChatSession as ChatSessionDto
    }

    // get all messages for a session id
    @GetMapping("/sessions/{sessionId}/messages")
    public ResponseEntity<List<ChatMessageDto>> getMessages(
            @PathVariable UUID sessionId
    ){
        return ResponseEntity.ok(chatService.getSessionMessages(sessionId));
    }

    // post a new message and get the AI response
    @PostMapping("/sessions/{sessionId}/messages")
    public ResponseEntity<ChatMessageDto> sendMessage(
            @PathVariable UUID sessionId,
            @AuthenticationPrincipal User user,
            @Valid @RequestBody SendMessageRequest request
    ){
        return ResponseEntity.ok(chatService.sendMessage(sessionId,user,request));
    }
}
