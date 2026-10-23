
// 1. AUTHENTICATION CONTRACTS

export interface User {
    id: string;
    email: string;
    username?: string;
}

export interface AuthResponse {
    token: string;
}

export interface LoginRequest {
    email: string;
    password: string;
}

export interface RegisterRequest {
    email: string;
    username: string;
    password: string;
}


// 2. GAME & PGN CONTRACTS

export interface GameSummaryDto {
    gameId: string;
    whitePlayer: string;
    blackPlayer: string;
    event: string | null;
    gameDate: string | null;
    result: string;
    uploadedAt: string;
}

export interface GameDetailDto {
    gameId: string;
    whitePlayer: string;
    blackPlayer: string;
    event: string | null;
    gameDate: string | null;
    result: string;
    uploadedAt: string;
    pgn: string;
    moves: string[];
    fens: string[];
}

export interface GameUploadResponse {
    gameId: string;
    whitePlayer: string;
    blackPlayer: string;
    result: string;
    totalMoves: number;
    message: string;
}


// 3. STOCKFISH ANALYSIS CONTRACTS

export interface EvaluationResult {
    centipawns: number | null;
    mateInMoves: number | null;
    bestMove: string | null;
    principalVariation: string | null;
}


// 4. CHAT & AI COACH CONTRACTS

export type MessageRole = 'USER' | 'ASSISTANT';

export interface ChatSessionDto {
    id: string;
    gameId: string | null;
    userId: string;
    createdAt: string;
}

export interface ChatMessageDto {
    id: string;
    role: MessageRole;
    content: string;
    moveContextFen: string | null;
    boardVariation: string | null; // JSON string array of UCI moves, e.g. '["e2e4", "e7e5"]'
    createdAt: string;
}

export interface SendMessageRequest {
    content: string;
    moveContextFen?: string | null;
}