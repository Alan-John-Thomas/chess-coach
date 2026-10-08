package com.chesscoach.backend.chat.dto;

import jakarta.validation.constraints.NotBlank;

// DTO sent to server when user types and click sent in chat box
public record SendMessageRequest(
        @NotBlank(message = "Message content cannot be empty")
        String content,
        String moveContextFen
) {}
