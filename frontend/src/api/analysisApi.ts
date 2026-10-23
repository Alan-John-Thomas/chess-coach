import client from './client';
import { EvaluationResult } from '../types';

export const analysisApi = {
    // Call Stockfish to evaluate a position (GET /api/analysis?fen=...&depth=18)
    analyzePosition: async (fen: string, depth = 18): Promise<EvaluationResult> => {
        const response = await client.get<EvaluationResult>('/analysis', {
            params: { fen, depth },
        });
        return response.data;
    }
};