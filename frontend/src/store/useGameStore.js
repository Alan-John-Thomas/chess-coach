import { create } from 'zustand';

export const useGameStore = create((set) => ({
  currentGame: null,
  moves: [],            // List of moves in the game
  currentMoveIndex: -1, // The move number the user is currently looking at
  currentFen: 'rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1', // Starts at starting position

  // Action to load a game
  setGame: (game, moves) => set({ 
    currentGame: game, 
    moves: moves, 
    currentMoveIndex: -1,
    currentFen: 'rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1'
  }),

  // Action to navigate to a specific move index
  selectMove: (index, fen) => set({ 
    currentMoveIndex: index,
    currentFen: fen
  }),

  // Action to clear active game
  clearGame: () => set({ 
    currentGame: null, 
    moves: [], 
    currentMoveIndex: -1,
    currentFen: 'rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1'
  }),
}));
