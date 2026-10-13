import React from 'react';
import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom';
import LoginPage from './pages/LoginPage';
import GamesPage from './pages/GamesPage';
import AnalysisPage from './pages/AnalysisPage';

function App() {
  return (
    <BrowserRouter>
      <Routes>
        {/* Auth routes */}
        <Route path="/login" element={<LoginPage />} />
        
        {/* Core app routes */}
        <Route path="/games" element={<GamesPage />} />
        <Route path="/analysis" element={<AnalysisPage />} />
        
        {/* Fallback redirect to login */}
        <Route path="*" element={<Navigate to="/login" replace />} />
      </Routes>
    </BrowserRouter>
  );
}

export default App;
