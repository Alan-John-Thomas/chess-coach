import React from 'react';
import { useNavigate } from 'react-router-dom';
import { useAuthStore } from '../store/useAuthStore';

export default function LoginPage() {
  const navigate = useNavigate();
  const login = useAuthStore((state) => state.login);

  const handleMockLogin = () => {
    // Set mock user data in our Zustand global state
    login({ email: 'user@example.com' }, 'mock-jwt-token-12345');
    // Navigate to /games dashboard
    navigate('/games');
  };

  return (
    <div style={styles.container}>
      <div style={styles.card}>
        <h1 style={styles.title}>♟️ Chess Coach</h1>
        <p style={styles.subtitle}>Improve your game with Stockfish evaluation and conversational AI.</p>
        <button style={styles.button} onClick={handleMockLogin}>
          Sign In (Mock)
        </button>
      </div>
    </div>
  );
}

const styles = {
  container: {
    display: 'flex',
    justifyContent: 'center',
    alignItems: 'center',
    minHeight: '100vh',
    backgroundColor: '#121212',
    color: '#ffffff',
    fontFamily: 'system-ui, sans-serif',
  },
  card: {
    backgroundColor: '#1e1e1e',
    padding: '3rem',
    borderRadius: '12px',
    boxShadow: '0 8px 24px rgba(0,0,0,0.5)',
    textAlign: 'center',
    maxWidth: '400px',
  },
  title: {
    margin: '0 0 1rem 0',
    fontSize: '2.5rem',
  },
  subtitle: {
    color: '#aaa',
    marginBottom: '2rem',
    lineHeight: '1.5',
  },
  button: {
    backgroundColor: '#0070f3',
    color: '#fff',
    border: 'none',
    padding: '0.8rem 2rem',
    fontSize: '1rem',
    borderRadius: '6px',
    cursor: 'pointer',
    width: '100%',
    fontWeight: 'bold',
  },
};
