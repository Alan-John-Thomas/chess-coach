package com.chesscoach.backend.analysis.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;

@Entity
@Table(name = "position_cache")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder // can use constructor with only some of arguments defined , argument order is auto mapped

// NOTE: position cache currently doesn't store mate in moves data.
// if evaluation is taken from this cache table , on EvaluationResult (dto) make mate in move as NULL.
// similarly when storing this cache , don't store mate in move
// analysis service currently doesn't output toplines,move classification.These columns are NULL for now.
public class PositionCache {
    @Id
    @Column(nullable = false,length = 150)
    private String fen;
    @Column
    // centipawn
    private Double evaluation;
    @Column(name="best_move")
    private String bestMove;
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name="top_lines",columnDefinition = "jsonb")
    private String topLines;
    @Column(length = 20)
    private String classification;
    @CreationTimestamp
    @Column(name = "analysed_at",updatable = false)
    private LocalDateTime analysedAt;
}
