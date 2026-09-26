package com.chesscoach.backend.chat.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BoardVariationServiceTest {

    private BoardVariationService boardVariationService;

    @BeforeEach
    void setUp() {
        boardVariationService = new BoardVariationService(new ObjectMapper());
    }

    @Test
    void formatToJson_ValidPv_ReturnsCappedJsonArray() {
        String rawPv = "e2e4 e7e5 g1f3 b8c6 f1b5 a7a6";

        // Limit to 4 moves
        String result = boardVariationService.formatToJson(rawPv, 4);

        assertNotNull(result);
        assertEquals("[\"e2e4\",\"e7e5\",\"g1f3\",\"b8c6\"]", result);
    }

    @Test
    void formatToJson_FewerMovesThanCap_ReturnsAllMoves() {
        String rawPv = "e2e4 e7e5";

        // Cap is 5, but we only have 2
        String result = boardVariationService.formatToJson(rawPv, 5);

        assertNotNull(result);
        assertEquals("[\"e2e4\",\"e7e5\"]", result);
    }

    @Test
    void formatToJson_NullOrBlank_ReturnsNull() {
        assertNull(boardVariationService.formatToJson(null, 5));
        assertNull(boardVariationService.formatToJson("   ", 5));
        assertNull(boardVariationService.formatToJson("", 5));
    }
}