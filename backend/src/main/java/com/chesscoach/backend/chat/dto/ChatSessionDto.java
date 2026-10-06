package com.chesscoach.backend.chat.dto;

import com.chesscoach.backend.chat.entity.ChatSession;

import java.time.LocalDateTime;
import java.util.UUID;

public record ChatSessionDto(
        UUID id,
        UUID gameId,
        UUID userId,
        LocalDateTime createdAt
) {
    public static ChatSessionDto fromEntity(ChatSession session) {
        return new ChatSessionDto(
                session.getId(),
                session.getGame().getId(),
                session.getUser().getId(),
                session.getCreatedAt()
        );
    }
}