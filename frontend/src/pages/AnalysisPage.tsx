import React from 'react';
import { useNavigate } from 'react-router-dom';
import ChessboardPanel from '../components/board/ChessboardPanel';
import { useAuthStore } from '../store/useAuthStore';

export default function AnalysisPage() {
  const navigate = useNavigate();
  const logout = useAuthStore((state) => state.logout);
  const user = useAuthStore((state) => state.user);

  const handleLogout = () => {
    logout();
    navigate('/login');
  };

  return (
    <div className="flex flex-col h-screen w-screen bg-obsidian text-white overflow-hidden select-none font-sans">
      {/* Top Navigation Bar */}
      <header className="h-14 border-b border-white/10 px-6 flex items-center justify-between bg-panel shrink-0">
        <div className="flex items-center gap-3">
          <span className="text-xl">♟️</span>
          <span className="font-bold text-base tracking-tight text-white/95">Chess Coach</span>
          <span className="text-xs px-2 py-0.5 rounded-full bg-blue-500/10 text-blue-400 border border-blue-500/20 font-medium">
            Studio
          </span>
        </div>

        <div className="flex items-center gap-4 text-xs">
          <button
            onClick={() => navigate('/games')}
            className="text-white/60 hover:text-white transition cursor-pointer"
          >
            ← My Games
          </button>
          <div className="w-[1px] h-4 bg-white/10" />
          <span className="text-white/50">{user?.email || 'Logged In'}</span>
          <button
            onClick={handleLogout}
            className="px-2.5 py-1 bg-surface hover:bg-surface-hover border border-white/10 rounded-md text-white/70 hover:text-white transition cursor-pointer"
          >
            Logout
          </button>
        </div>
      </header>

      {/* Main 3-Column Studio Layout (3 : 5 : 4) */}
      <main className="flex-1 grid grid-cols-12 gap-4 p-4 overflow-hidden min-h-0">
        {/* Column 1: Analysis & Move List (3 cols) */}
        <aside className="col-span-3 bg-panel border border-white/10 rounded-2xl p-4 flex flex-col overflow-hidden shadow-2xl">
          <div className="flex items-center justify-between pb-3 border-b border-white/5 mb-3">
            <h2 className="text-xs font-semibold text-white/50 uppercase tracking-wider">Evaluation & Moves</h2>
            <span className="w-2 h-2 rounded-full bg-emerald-400 animate-pulse" title="Engine Ready" />
          </div>

          <div className="flex-1 flex flex-col justify-center items-center text-center p-4 border border-dashed border-white/10 rounded-xl bg-surface/30">
            <div className="text-2xl mb-2">📊</div>
            <div className="text-sm font-medium text-white/80">Stockfish Engine</div>
            <p className="text-xs text-white/40 mt-1 max-w-[200px]">
              Eval bar and move classifications will render here in Day 3.
            </p>
          </div>
        </aside>

        {/* Column 2: Interactive Chessboard (5 cols) */}
        <section className="col-span-5 bg-panel border border-white/10 rounded-2xl p-6 flex flex-col items-center justify-center overflow-hidden shadow-2xl">
          <ChessboardPanel />
        </section>

        {/* Column 3: Raycast AI Coach Chat (4 cols) */}
        <aside className="col-span-4 bg-panel border border-white/10 rounded-2xl p-4 flex flex-col overflow-hidden shadow-2xl">
          <div className="flex items-center justify-between pb-3 border-b border-white/5 mb-3">
            <h2 className="text-xs font-semibold text-white/50 uppercase tracking-wider">Raycast AI Coach</h2>
            <span className="text-[10px] px-2 py-0.5 rounded bg-purple-500/10 text-purple-400 border border-purple-500/20 font-medium">
              DeepSeek-R1
            </span>
          </div>

          <div className="flex-1 flex flex-col justify-center items-center text-center p-4 border border-dashed border-white/10 rounded-xl bg-surface/30">
            <div className="text-2xl mb-2">🧠</div>
            <div className="text-sm font-medium text-white/80">Conversational Grandmaster</div>
            <p className="text-xs text-white/40 mt-1 max-w-[240px]">
              Streaming conversational coaching will render here in Day 4.
            </p>
          </div>
        </aside>
      </main>
    </div>
  );
}
