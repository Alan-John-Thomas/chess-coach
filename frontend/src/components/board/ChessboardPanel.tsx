import React, { useEffect, useMemo, useState } from 'react';
import { Chessboard } from 'react-chessboard';
import { Chess } from 'chess.js';
import { useGameStore } from '../../store/useGameStore';

export default function ChessboardPanel() {
  // Zustand Store Selectors
  const currentFen = useGameStore((state) => state.currentFen);
  const orientation = useGameStore((state) => state.orientation);
  const currentMoveIndex = useGameStore((state) => state.currentMoveIndex);
  const totalMoves = useGameStore((state) => state.moves.length);

  // Zustand Store Actions
  const makeMove = useGameStore((state) => state.makeMove);
  const nextMove = useGameStore((state) => state.nextMove);
  const prevMove = useGameStore((state) => state.prevMove);
  const firstMove = useGameStore((state) => state.firstMove);
  const lastMove = useGameStore((state) => state.lastMove);
  const flipBoard = useGameStore((state) => state.flipBoard);

  // Local selection state for Click-to-Move & Legal Move Dots
  const [selectedSquare, setSelectedSquare] = useState<string | null>(null);

  // In-memory chess referee for querying checks and legal targets
  const chess = useMemo(() => new Chess(currentFen), [currentFen]);

  // Find King square if currently in check
  const checkSquare = useMemo(() => {
    if (!chess.inCheck()) return null;
    const turn = chess.turn();
    const board = chess.board();
    for (let r = 0; r < 8; r++) {
      for (let c = 0; c < 8; c++) {
        const piece = board[r][c];
        if (piece && piece.type === 'k' && piece.color === turn) {
          return `${String.fromCharCode(97 + c)}${8 - r}`;
        }
      }
    }
    return null;
  }, [chess]);

  // Compute legal destination squares for the currently selected piece
  const legalMoveSquares = useMemo(() => {
    if (!selectedSquare) return [];
    try {
      const moves = chess.moves({ square: selectedSquare as any, verbose: true });
      return moves.map((m) => ({
        square: m.to,
        isCapture: m.captured !== undefined,
      }));
    } catch {
      return [];
    }
  }, [chess, selectedSquare]);

  // Handle Drag & Drop move
  const handlePieceDrop = ({ sourceSquare, targetSquare }: { sourceSquare: string; targetSquare: string | null }): boolean => {
    if (!targetSquare) return false;
    const success = makeMove(sourceSquare, targetSquare);
    setSelectedSquare(null);
    return success;
  };

  // Handle Square Click (for Click-to-Move interaction)
  const handleSquareClick = ({ square }: { square: string }) => {
    // 1. If a piece was already selected and clicked square is a legal destination
    if (selectedSquare) {
      const isLegal = legalMoveSquares.some((m) => m.square === square);
      if (isLegal) {
        makeMove(selectedSquare, square);
        setSelectedSquare(null);
        return;
      }
    }

    // 2. If clicking on own piece, select it and show legal move dots
    const piece = chess.get(square as any);
    if (piece && piece.color === chess.turn()) {
      setSelectedSquare(square);
    } else {
      setSelectedSquare(null);
    }
  };

  // Build custom square styles (Dots, Check Glow, Selection Highlight)
  const customSquareStyles = useMemo(() => {
    const styles: Record<string, React.CSSProperties> = {};

    // 1. King in Check (Crimson Glow)
    if (checkSquare) {
      styles[checkSquare] = {
        background: 'radial-gradient(circle, rgba(239, 68, 68, 0.85) 0%, rgba(239, 68, 68, 0.35) 60%, transparent 85%)',
        borderRadius: '8px',
      };
    }

    // 2. Selected Piece Highlight
    if (selectedSquare) {
      styles[selectedSquare] = {
        backgroundColor: 'rgba(59, 130, 246, 0.35)', // Blue tint
      };
    }

    // 3. Legal Move Dots & Capture Rings
    for (const { square, isCapture } of legalMoveSquares) {
      styles[square] = isCapture
        ? {
            background: 'radial-gradient(circle, transparent 52%, rgba(239, 68, 68, 0.55) 53%, rgba(239, 68, 68, 0.55) 70%, transparent 71%)',
            cursor: 'pointer',
          }
        : {
            background: 'radial-gradient(circle, rgba(255, 255, 255, 0.35) 24%, transparent 25%)',
            cursor: 'pointer',
          };
    }

    return styles;
  }, [checkSquare, selectedSquare, legalMoveSquares]);

  // Keyboard navigation shortcuts
  useEffect(() => {
    const handleKeyDown = (e: KeyboardEvent) => {
      if (e.target instanceof HTMLInputElement || e.target instanceof HTMLTextAreaElement) return;

      if (e.key === 'ArrowLeft') {
        e.preventDefault();
        setSelectedSquare(null);
        prevMove();
      } else if (e.key === 'ArrowRight') {
        e.preventDefault();
        setSelectedSquare(null);
        nextMove();
      } else if (e.key === 'ArrowUp') {
        e.preventDefault();
        setSelectedSquare(null);
        firstMove();
      } else if (e.key === 'ArrowDown') {
        e.preventDefault();
        setSelectedSquare(null);
        lastMove();
      } else if (e.key.toLowerCase() === 'f') {
        e.preventDefault();
        flipBoard();
      }
    };

    window.addEventListener('keydown', handleKeyDown);
    return () => window.removeEventListener('keydown', handleKeyDown);
  }, [nextMove, prevMove, firstMove, lastMove, flipBoard]);

  const isAtStart = currentMoveIndex <= -1;
  const isAtEnd = totalMoves === 0 || currentMoveIndex >= totalMoves - 1;

  return (
    <div className="flex flex-col items-center justify-center gap-4 w-full max-w-[560px] mx-auto select-none">
      {/* 1. Interactive Chessboard Area */}
      <div className="w-full aspect-square flex items-center justify-center">
        <Chessboard
          options={{
            position: currentFen,
            boardOrientation: orientation,
            darkSquareStyle: { backgroundColor: '#2B3245' },
            lightSquareStyle: { backgroundColor: '#475270' },
            boardStyle: {
              borderRadius: '12px',
              boxShadow: '0 20px 48px rgba(0, 0, 0, 0.7)',
              border: '1px solid rgba(255, 255, 255, 0.1)',
            },
            squareStyles: customSquareStyles,
            animationDurationInMs: 200,
            onPieceDrop: handlePieceDrop,
            onSquareClick: handleSquareClick,
          }}
        />
      </div>

      {/* 2. Raycast-style Navigation Dock */}
      <div className="flex items-center justify-between w-full bg-panel border border-white/10 rounded-xl px-4 py-2.5 shadow-xl">
        <div className="flex items-center gap-1.5">
          <button
            onClick={firstMove}
            disabled={isAtStart}
            className="px-2.5 py-1.5 bg-surface hover:bg-surface-hover active:scale-95 border border-white/10 rounded-lg text-sm text-white transition disabled:opacity-30 disabled:cursor-not-allowed"
            title="First Move (Arrow Up)"
          >
            ⏮
          </button>
          <button
            onClick={prevMove}
            disabled={isAtStart}
            className="px-2.5 py-1.5 bg-surface hover:bg-surface-hover active:scale-95 border border-white/10 rounded-lg text-sm text-white transition disabled:opacity-30 disabled:cursor-not-allowed"
            title="Previous Move (Arrow Left)"
          >
            ◀
          </button>

          <span className="text-xs font-semibold text-white/70 min-w-[100px] text-center font-mono tabular-nums">
            {currentMoveIndex === -1 ? 'Start' : `Move ${currentMoveIndex + 1} / ${totalMoves}`}
          </span>

          <button
            onClick={nextMove}
            disabled={isAtEnd}
            className="px-2.5 py-1.5 bg-surface hover:bg-surface-hover active:scale-95 border border-white/10 rounded-lg text-sm text-white transition disabled:opacity-30 disabled:cursor-not-allowed"
            title="Next Move (Arrow Right)"
          >
            ▶
          </button>
          <button
            onClick={lastMove}
            disabled={isAtEnd}
            className="px-2.5 py-1.5 bg-surface hover:bg-surface-hover active:scale-95 border border-white/10 rounded-lg text-sm text-white transition disabled:opacity-30 disabled:cursor-not-allowed"
            title="Last Move (Arrow Down)"
          >
            ⏭
          </button>
        </div>

        <div className="w-[1px] h-6 bg-white/10 mx-2" />

        <button
          onClick={flipBoard}
          className="px-3 py-1.5 bg-surface hover:bg-surface-hover active:scale-95 border border-white/10 rounded-lg text-xs font-semibold text-white/80 transition cursor-pointer flex items-center gap-1.5"
          title="Flip Board (F)"
        >
          🔄 {orientation === 'white' ? 'White' : 'Black'}
        </button>
      </div>
    </div>
  );
}
