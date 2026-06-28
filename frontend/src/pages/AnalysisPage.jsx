import React from 'react';

export default function AnalysisPage() {
  return (
    <div style={styles.container}>
      {/* Navbar */}
      <div style={styles.navbar}>
        <div style={{ fontWeight: 'bold', fontSize: '1.2rem' }}>♟️ Chess Coach</div>
        <div style={{ color: '#aaa' }}>Games | Profile | Logout (Mock)</div>
      </div>

      {/* Main 3-Panel Layout */}
      <div style={styles.workspace}>
        {/* Panel 1: Chessboard Placeholder */}
        <div style={{ ...styles.panel, flex: 2 }}>
          <h2 style={styles.panelTitle}>Chessboard</h2>
          <div style={styles.boardMock}>
            <span style={{ fontSize: '3rem' }}>🏁</span>
            <p style={{ marginTop: '1rem', color: '#888' }}>Interactive Chessboard will render here</p>
          </div>
          <div style={styles.controlsMock}>
            ◀ First | ◀ Prev | Next ▶ | Last ▶
          </div>
        </div>

        {/* Panel 2: Stockfish Panel Placeholder */}
        <div style={{ ...styles.panel, flex: 1.2 }}>
          <h2 style={styles.panelTitle}>Stockfish Engine</h2>
          <div style={styles.evalBarMock}>
            <div style={{ height: '50%', backgroundColor: '#fff', color: '#000', display: 'flex', alignItems: 'center', justifyContent: 'center', fontSize: '0.9rem' }}>
              +1.4
            </div>
            <div style={{ height: '50%', backgroundColor: '#000' }} />
          </div>
          <div style={styles.analysisDetails}>
            <span style={styles.badge}>⚠️ Inaccuracy</span>
            <p style={{ marginTop: '1rem' }}><strong>Best Move:</strong> Rd1</p>
            <p style={{ color: '#888', fontSize: '0.9rem' }}>
              Line 1: Rd1 Qc7 2. Bf4 e5<br />
              Line 2: Bf4 e5 2. Bxe5 Nxe5
            </p>
          </div>
        </div>

        {/* Panel 3: AI Coach Panel Placeholder */}
        <div style={{ ...styles.panel, flex: 1.5 }}>
          <h2 style={styles.panelTitle}>AI Coach Chat</h2>
          <div style={styles.chatBox}>
            <div style={styles.messageBubbleUser}>
              Why was my move on move 23 bad?
            </div>
            <div style={styles.messageBubbleCoach}>
              Your knight on f6 blocks the f-pawn, which stops you from establishing pawn structure control. A better play was Rd1 to secure the open file.
            </div>
          </div>
          <div style={styles.chatInputMock}>
            <input type="text" placeholder="Ask coach..." style={styles.input} disabled />
            <button style={styles.sendBtn}>Ask</button>
          </div>
        </div>
      </div>
    </div>
  );
}

const styles = {
  container: {
    display: 'flex',
    flexDirection: 'column',
    height: '100vh',
    backgroundColor: '#121212',
    color: '#ffffff',
    fontFamily: 'system-ui, sans-serif',
    overflow: 'hidden',
  },
  navbar: {
    height: '60px',
    borderBottom: '1px solid #333',
    display: 'flex',
    justifyContent: 'space-between',
    alignItems: 'center',
    padding: '0 2rem',
    backgroundColor: '#1a1a1a',
  },
  workspace: {
    display: 'flex',
    flex: 1,
    padding: '1rem',
    gap: '1rem',
    overflow: 'hidden',
  },
  panel: {
    backgroundColor: '#1e1e1e',
    borderRadius: '8px',
    border: '1px solid #333',
    padding: '1.2rem',
    display: 'flex',
    flexDirection: 'column',
    overflow: 'hidden',
  },
  panelTitle: {
    margin: '0 0 1rem 0',
    fontSize: '1.1rem',
    color: '#888',
    textTransform: 'uppercase',
    letterSpacing: '1px',
  },
  boardMock: {
    flex: 1,
    backgroundColor: '#151515',
    borderRadius: '6px',
    border: '1px solid #222',
    display: 'flex',
    flexDirection: 'column',
    justifyContent: 'center',
    alignItems: 'center',
  },
  controlsMock: {
    marginTop: '1rem',
    textAlign: 'center',
    padding: '0.8rem',
    backgroundColor: '#252525',
    borderRadius: '4px',
    fontSize: '0.9rem',
    color: '#aaa',
  },
  evalBarMock: {
    height: '120px',
    border: '1px solid #444',
    borderRadius: '4px',
    overflow: 'hidden',
    display: 'flex',
    flexDirection: 'column',
    marginBottom: '1rem',
  },
  analysisDetails: {
    backgroundColor: '#151515',
    padding: '1rem',
    borderRadius: '6px',
    flex: 1,
  },
  badge: {
    backgroundColor: '#b58900',
    color: '#fff',
    padding: '0.2rem 0.6rem',
    borderRadius: '4px',
    fontSize: '0.8rem',
    fontWeight: 'bold',
  },
  chatBox: {
    flex: 1,
    backgroundColor: '#151515',
    borderRadius: '6px',
    padding: '1rem',
    overflowY: 'auto',
    display: 'flex',
    flexDirection: 'column',
    gap: '1rem',
    marginBottom: '1rem',
  },
  messageBubbleUser: {
    alignSelf: 'flex-end',
    backgroundColor: '#0070f3',
    color: '#fff',
    padding: '0.8rem',
    borderRadius: '8px 8px 0 8px',
    maxWidth: '80%',
    fontSize: '0.9rem',
  },
  messageBubbleCoach: {
    alignSelf: 'flex-start',
    backgroundColor: '#252525',
    color: '#fff',
    padding: '0.8rem',
    borderRadius: '8px 8px 8px 0',
    maxWidth: '80%',
    fontSize: '0.9rem',
    lineHeight: '1.4',
  },
  chatInputMock: {
    display: 'flex',
    gap: '0.5rem',
  },
  input: {
    flex: 1,
    backgroundColor: '#222',
    border: '1px solid #333',
    borderRadius: '4px',
    padding: '0.6rem',
    color: '#fff',
  },
  sendBtn: {
    backgroundColor: '#333',
    color: '#aaa',
    border: 'none',
    padding: '0 1.2rem',
    borderRadius: '4px',
    cursor: 'not-allowed',
  },
};
