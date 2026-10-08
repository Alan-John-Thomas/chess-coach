// DTO representing a chat , Both the AI, user chats use this Dto
// This goes back to the frontend and is used to render previous chats bubbles

package com.chesscoach.backend.chat.dto;

import com.chesscoach.backend.chat.entity.ChatMessage;
import com.chesscoach.backend.chat.entity.MessageRole;

import java.time.LocalDateTime;
import java.util.UUID;

public record ChatMessageDto(
        UUID id,
        MessageRole role,
        String content,
        String moveContextFen,
        // used by Coach to show board variation when explaining
        String boardVariation,
        LocalDateTime createdAt
) {
    // Helper to easily convert database entity (ChatMessage row) into this DTO
    public static ChatMessageDto fromEntity(ChatMessage msg) {
        return new ChatMessageDto(
                msg.getId(),
                msg.getRole(),
                msg.getContent(),
                msg.getMoveContextFen(),
                msg.getBoardVariation(),
                msg.getCreatedAt()
        );
    }
}
