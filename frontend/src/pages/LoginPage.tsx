import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { useAuthStore } from '../store/useAuthStore';
import { authApi } from '../api/authApi';

export default function LoginPage() {
  const navigate = useNavigate();
  const login = useAuthStore((state) => state.login);

  const [isRegister, setIsRegister] = useState(false);
  const [email, setEmail] = useState('');
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setError(null);
    setLoading(true);

    try {
      if (isRegister) {
        // 1. Register new user against Spring Boot
        await authApi.register({ email, username, password });
      }

      // 2. Authenticate against Spring Boot /api/auth/login
      const { token } = await authApi.login({ email, password });

      // 3. Hydrate Zustand store & persist to localStorage
      login({ id: email, email, username: isRegister ? username : email.split('@')[0] }, token);

      // 4. Redirect to games dashboard
      navigate('/games');
    } catch (err: any) {
      const message = err.response?.data?.message || err.message || 'Authentication failed';
      setError(message);
    } finally {
      setLoading(false);
    }
  };

  return (
      <div style={styles.container}>
        <div style={styles.card}>
          <h1 style={styles.title}>♟️ Chess Coach</h1>
          <p style={styles.subtitle}>
            {isRegister ? 'Create an account to start analyzing' : 'Sign in to access your game analysis'}
          </p>

          {error && <div style={styles.errorAlert}>{error}</div>}

          <form onSubmit={handleSubmit} style={styles.form}>
            {isRegister && (
                <div style={styles.inputGroup}>
                  <label style={styles.label}>Username</label>
                  <input
                      type="text"
                      required
                      value={username}
                      onChange={(e) => setUsername(e.target.value)}
                      style={styles.input}
                      placeholder="grandmaster"
                  />
                </div>
            )}

            <div style={styles.inputGroup}>
              <label style={styles.label}>Email</label>
              <input
                  type="email"
                  required
                  value={email}
                  onChange={(e) => setEmail(e.target.value)}
                  style={styles.input}
                  placeholder="user@example.com"
              />
            </div>

            <div style={styles.inputGroup}>
              <label style={styles.label}>Password</label>
              <input
                  type="password"
                  required
                  value={password}
                  onChange={(e) => setPassword(e.target.value)}
                  style={styles.input}
                  placeholder="••••••••"
              />
            </div>

            <button type="submit" disabled={loading} style={styles.button}>
              {loading ? 'Connecting...' : isRegister ? 'Register & Sign In' : 'Sign In'}
            </button>
          </form>

          <button
              type="button"
              onClick={() => {
                setIsRegister(!isRegister);
                setError(null);
              }}
              style={styles.toggleButton}
          >
            {isRegister ? 'Already have an account? Sign In' : "Don't have an account? Register"}
          </button>
        </div>
      </div>
  );
}

const styles: Record<string, React.CSSProperties> = {
  container: {
    display: 'flex',
    justifyContent: 'center',
    alignItems: 'center',
    minHeight: '100vh',
    backgroundColor: '#0B0D13',
    color: '#ffffff',
    fontFamily: 'system-ui, -apple-system, sans-serif',
  },
  card: {
    backgroundColor: '#131722',
    border: '1px solid rgba(255, 255, 255, 0.08)',
    padding: '2.5rem',
    borderRadius: '16px',
    boxShadow: '0 12px 32px rgba(0, 0, 0, 0.6)',
    width: '100%',
    maxWidth: '400px',
  },
  title: {
    margin: '0 0 0.5rem 0',
    fontSize: '2rem',
    fontWeight: 700,
    textAlign: 'center',
  },
  subtitle: {
    color: 'rgba(255, 255, 255, 0.6)',
    fontSize: '0.875rem',
    marginBottom: '1.5rem',
    textAlign: 'center',
    lineHeight: 1.4,
  },
  errorAlert: {
    backgroundColor: 'rgba(239, 68, 68, 0.1)',
    border: '1px solid rgba(239, 68, 68, 0.4)',
    color: '#f87171',
    padding: '0.75rem',
    borderRadius: '8px',
    marginBottom: '1rem',
    fontSize: '0.85rem',
    textAlign: 'center',
  },
  form: {
    display: 'flex',
    flexDirection: 'column',
    gap: '1rem',
  },
  inputGroup: {
    display: 'flex',
    flexDirection: 'column',
    gap: '0.35rem',
    textAlign: 'left',
  },
  label: {
    fontSize: '0.8rem',
    fontWeight: 600,
    color: 'rgba(255, 255, 255, 0.75)',
  },
  input: {
    backgroundColor: '#0B0D13',
    border: '1px solid rgba(255, 255, 255, 0.12)',
    borderRadius: '8px',
    padding: '0.7rem 0.85rem',
    color: '#ffffff',
    fontSize: '0.9rem',
    outline: 'none',
  },
  button: {
    backgroundColor: '#3b82f6',
    color: '#ffffff',
    border: 'none',
    padding: '0.8rem',
    fontSize: '0.95rem',
    borderRadius: '8px',
    fontWeight: 600,
    cursor: 'pointer',
    marginTop: '0.5rem',
  },
  toggleButton: {
    background: 'transparent',
    border: 'none',
    color: 'rgba(255, 255, 255, 0.5)',
    fontSize: '0.8rem',
    marginTop: '1.25rem',
    cursor: 'pointer',
    width: '100%',
    textAlign: 'center',
    textDecoration: 'underline',
  },
};