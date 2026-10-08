package com.chesscoach.backend.chat.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class BoardVariationService {
    private final ObjectMapper objectMapper;
    /**
     * Converts a raw Stockfish PV string (e.g. "e2e4 e7e5 g1f3 b8c6")
     * into a valid JSON array string (e.g. ["e2e4","e7e5","g1f3","b8c6"])
     * capped to maxMoves for clean visual playback on the frontend.
     */
    public String formatToJson(String s,int maxMoves){
        if (s==null||s.isBlank()){
            return null;
        }
        try {
            List<String> moves = Arrays.stream(s.trim().split("\\s+")) // split one or more whitespaces
                    .filter(move -> !move.isBlank()) // only take if move (individual part of stream) is not blank
                    .limit(maxMoves)
                    .toList();
            if (moves.isEmpty()) {
                return null;
            }
            return objectMapper.writeValueAsString(moves); // convert List<string> to string
        } catch (JsonProcessingException e) {
            log.error("Failed to serialize board variation to JSON: {}", s, e);
            return null;
        }
    }
}
