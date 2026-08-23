package com.chesscoach.backend.llm.controller;

import com.chesscoach.backend.llm.dto.CoachExplanationDto;
import com.chesscoach.backend.llm.service.CoachService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/coach")
@RequiredArgsConstructor
public class CoachController {
    private final CoachService coachService;
    @GetMapping("/explain")
    public ResponseEntity<CoachExplanationDto>explainPosition(
            @RequestParam String fen,
            @RequestParam(defaultValue = "18") Integer depth,
            @RequestParam(required = false) String question
    ){
        CoachExplanationDto response = coachService.explainPosition(fen,depth,question);
        return ResponseEntity.ok(response);
    }
}
