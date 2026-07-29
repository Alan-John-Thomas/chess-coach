package com.chesscoach.backend.analysis.controller;

import com.chesscoach.backend.analysis.dto.EvaluationResult;
import com.chesscoach.backend.analysis.service.AnalysisService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/analysis")
@RequiredArgsConstructor
public class AnalysisController {

    private final AnalysisService analysisService;

    @GetMapping
    public ResponseEntity<EvaluationResult> analyzePosition(
            @RequestParam String fen,
            @RequestParam(defaultValue = "18") int depth
    ){
        EvaluationResult result = analysisService.analysePosition(fen,depth);
        return ResponseEntity.ok(result);
    }
}
