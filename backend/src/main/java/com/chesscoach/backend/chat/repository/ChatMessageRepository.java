package com.chesscoach.backend.chat.repository;

import com.chesscoach.backend.chat.entity.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage, UUID> {
    // Chronological order (for full chat flow & LLM context)
    List<ChatMessage> findBySessionIdOrderByCreatedAtAsc(UUID sessionId);
    // Reverse chronological order (for latest message previews)
    List<ChatMessage> findBySessionIdOrderByCreatedAtDesc(UUID sessionId);
}
