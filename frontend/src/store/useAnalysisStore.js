import { create } from 'zustand';

export const useAnalysisStore = create((set) => ({
  evaluation: 0.0,      // Centipawn eval (e.g. +1.4)
  bestMove: '',         // Best engine move (e.g. 'Rd1')
  topLines: [],         // Top 3 engine continuations
  classification: '',   // Blunder, Mistake, Inaccuracy, Excellent, etc.
  isLoading: false,

  // Action to set analysis results
  setAnalysis: (analysisData) => set({
    evaluation: analysisData.evaluation,
    bestMove: analysisData.bestMove,
    topLines: analysisData.topLines,
    classification: analysisData.classification,
    isLoading: false
  }),

  setLoading: (loading) => set({ isLoading: loading }),
}));
