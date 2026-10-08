package com.chesscoach.backend.chat.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "chat_messages")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatMessage {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "session_id", nullable = false)
    private ChatSession session;

    // converts the enum type to string when saving in database
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private MessageRole role;

    // chat
    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    // clicking message loads this position on ui
    @Column(name = "move_context_fen", length = 150)
    private String moveContextFen;

    // board moves to render for an explanation
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "board_variation", columnDefinition = "jsonb")
    private String boardVariation;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
}
