import { create } from 'zustand';
import { Chess } from 'chess.js';
import { GameDetailDto } from '../types';

export const START_FEN = 'rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1';

export interface GameState {
  // State
  currentGame: GameDetailDto | null;
  moves: string[];
  fens: string[];
  currentMoveIndex: number; // -1 = start position, 0 = after move 1
  currentFen: string;
  orientation: 'white' | 'black';

  // Actions
  setGame: (game: GameDetailDto) => void;
  goToMove: (index: number) => void;
  nextMove: () => void;
  prevMove: () => void;
  firstMove: () => void;
  lastMove: () => void;
  flipBoard: () => void;
  resetGame: () => void;
  makeMove: (from: string, to: string, promotion?: string) => boolean;
}

// how the store is initialized on startup
// create fn is called with the arrow fn given below and zustand (create fn) will call this arrow fn with its set and get function
export const useGameStore = create<GameState>((set, get) => ({
  currentGame: null,
  moves: [],
  fens: [START_FEN],
  currentMoveIndex: -1,
  currentFen: START_FEN,
  orientation: 'white',

  // Load a full game from the backend (backend sends gameDetailDto)
  setGame: (game: GameDetailDto) => {
    const fens = game.fens && game.fens.length > 0 ? game.fens : [START_FEN];
    set({
      currentGame: game,
      moves: game.moves || [],
      fens: fens,
      currentMoveIndex: -1,
      currentFen: fens[0] || START_FEN,
      // Default orientation to white unless user played black
      orientation: 'white',
    });
  },

  // Jump directly to a move index (-1 to moves.length - 1)
  goToMove: (index: number) => {
    const { moves, fens } = get();
    if (index < -1 || index >= moves.length) return;

    // fens[0] is start position; fens[index + 1] is position after moves[index]
    const targetFen = fens[index + 1] || START_FEN;
    set({
      currentMoveIndex: index,
      currentFen: targetFen,
    });
  },

  // Advance 1 move forward
  nextMove: () => {
    const { currentMoveIndex, moves, goToMove } = get();
    if (currentMoveIndex < moves.length - 1) {
      goToMove(currentMoveIndex + 1);
    }
  },

  // Step 1 move backward
  prevMove: () => {
    const { currentMoveIndex, goToMove } = get();
    if (currentMoveIndex > -1) {
      goToMove(currentMoveIndex - 1);
    }
  },

  // Jump to start of game
  firstMove: () => {
    get().goToMove(-1);
  },

  // Jump to final position of game
  lastMove: () => {
    const { moves, goToMove } = get();
    if (moves.length > 0) {
      goToMove(moves.length - 1);
    }
  },

  // Toggle board perspective
  flipBoard: () => {
    set((state) => ({
      orientation: state.orientation === 'white' ? 'black' : 'white',
    }));
  },

  // Clear active game and reset to standard starting board
  resetGame: () => {
    set({
      currentGame: null,
      moves: [],
      fens: [START_FEN],
      currentMoveIndex: -1,
      currentFen: START_FEN,
      orientation: 'white',
    });
  },

  // Execute an interactive piece move on the active board
  makeMove: (from: string, to: string, promotion = 'q') => {
    const { currentFen, moves, fens, currentMoveIndex } = get();

    try {
      const chess = new Chess(currentFen);
      const moveResult = chess.move({
        from,
        to,
        promotion,
      });

      // Illegal move attempted
      if (!moveResult) return false;

      const newFen = chess.fen();
      const newMoveSan = moveResult.san;

      // Slice history up to current move (allows branching off variations)
      const updatedMoves = [...moves.slice(0, currentMoveIndex + 1), newMoveSan];
      const updatedFens = [...fens.slice(0, currentMoveIndex + 2), newFen];
      const newIndex = currentMoveIndex + 1;

      set({
        moves: updatedMoves,
        fens: updatedFens,
        currentMoveIndex: newIndex,
        currentFen: newFen,
      });

      return true;
    } catch {
      return false;
    }
  },
}));