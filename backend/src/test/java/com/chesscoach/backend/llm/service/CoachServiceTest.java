package com.chesscoach.backend.llm.service;

import com.chesscoach.backend.analysis.dto.EvaluationResult;
import com.chesscoach.backend.analysis.service.AnalysisService;
import com.chesscoach.backend.llm.dto.CoachExplanationDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CoachServiceTest {

    @Mock
    private AnalysisService analysisService;

    @Mock
    private ChessPromptBuilder chessPromptBuilder;

    @Mock
    private LlmService llmService;

    @InjectMocks
    private CoachService coachService;

    @Test
    void explainPosition_ReturnsCompleteCoachExplanationDto() {
        String fen = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";
        EvaluationResult evalResult = new EvaluationResult(0.25, null, "e2e4", "e2e4 e7e5");
        String mockPrompt = "mock prompt text";
        String mockExplanation = "White should control the center with e4.";

        when(analysisService.analysePosition(fen, 18)).thenReturn(evalResult);
        when(chessPromptBuilder.buildPrompt(eq(fen), eq(evalResult), anyString())).thenReturn(mockPrompt);
        when(llmService.generateResponse(mockPrompt)).thenReturn(mockExplanation);

        CoachExplanationDto result = coachService.explainPosition(fen, 18, "What is the plan?");

        assertNotNull(result);
        assertEquals(fen, result.fen());
        assertEquals(0.25, result.evaluation());
        assertEquals("e2e4", result.bestMove());
        assertEquals("e2e4 e7e5", result.principalVariation());
        assertEquals(mockExplanation, result.explanation());

        verify(analysisService, times(1)).analysePosition(fen, 18);
        verify(chessPromptBuilder, times(1)).buildPrompt(eq(fen), eq(evalResult), anyString());
        verify(llmService, times(1)).generateResponse(mockPrompt);
    }
}