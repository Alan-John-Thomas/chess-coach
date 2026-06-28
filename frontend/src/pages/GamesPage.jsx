import React from 'react';
import { useNavigate } from 'react-router-dom';

export default function GamesPage() {
  const navigate = useNavigate();
  const mockGames = [
    { id: '1', white: 'Player A', black: 'Player B', result: '1-0', date: '2026-06-21' },
    { id: '2', white: 'Player C', black: 'Player D', result: '0-1', date: '2026-06-20' },
  ];

  return (
    <div style={styles.container}>
      <h1 style={styles.title}>My Games</h1>
      <p style={styles.subtitle}>Upload PGN files and select a game to start analysis.</p>
      
      <div style={styles.uploadBox}>
        <span style={{ fontSize: '2rem' }}>📤</span>
        <p>Drag and drop your PGN file here, or click to upload</p>
      </div>

      <div style={styles.list}>
        {mockGames.map((game) => (
          <div key={game.id} style={styles.card}>
            <div>
              <strong>{game.white}</strong> vs <strong>{game.black}</strong>
              <div style={{ color: '#888', fontSize: '0.9rem', marginTop: '0.2rem' }}>
                Result: {game.result} | {game.date}
              </div>
            </div>
            <button style={styles.analyzeBtn} onClick={() => navigate('/analysis')}>Analyze</button>
          </div>
        ))}
      </div>
    </div>
  );
}

const styles = {
  container: {
    padding: '2rem',
    minHeight: '100vh',
    backgroundColor: '#121212',
    color: '#ffffff',
    fontFamily: 'system-ui, sans-serif',
  },
  title: {
    margin: '0 0 0.5rem 0',
  },
  subtitle: {
    color: '#aaa',
    marginBottom: '2rem',
  },
  uploadBox: {
    border: '2px dashed #333',
    borderRadius: '8px',
    padding: '3rem',
    textAlign: 'center',
    marginBottom: '2rem',
    backgroundColor: '#1a1a1a',
    cursor: 'pointer',
  },
  list: {
    display: 'flex',
    flexDirection: 'column',
    gap: '1rem',
  },
  card: {
    display: 'flex',
    justifyContent: 'space-between',
    alignItems: 'center',
    backgroundColor: '#1e1e1e',
    padding: '1.2rem',
    borderRadius: '8px',
    border: '1px solid #333',
  },
  analyzeBtn: {
    backgroundColor: '#0070f3',
    color: '#fff',
    border: 'none',
    padding: '0.5rem 1.2rem',
    borderRadius: '4px',
    cursor: 'pointer',
    fontWeight: 'bold',
  },
};
