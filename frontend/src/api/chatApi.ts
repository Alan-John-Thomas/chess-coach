import client from './client';
import { ChatMessageDto, ChatSessionDto, SendMessageRequest } from '../types';

export const chatApi = {
    // 1. Get or create a chat session for a game (POST /api/chat/sessions?gameId=...)
    getOrCreateSession: async (gameId: string): Promise<ChatSessionDto> => {
        // Note: 2nd arg is null because there is no @RequestBody, query param goes in 3rd arg
        const response = await client.post<ChatSessionDto>('/chat/sessions', null, {
            params: { gameId },
        });
        return response.data;
    },

    // 2. Fetch past conversation message history for a session (GET /api/chat/sessions/{sessionId}/messages)
    getSessionMessages: async (sessionId: string): Promise<ChatMessageDto[]> => {
        const response = await client.get<ChatMessageDto[]>(`/chat/sessions/${sessionId}/messages`);
        return response.data;
    },

    // 3. Send user question + board FEN to AI coach and get response (POST /api/chat/sessions/{sessionId}/messages)
    sendMessage: async (sessionId: string, request: SendMessageRequest): Promise<ChatMessageDto> => {
        const response = await client.post<ChatMessageDto>(`/chat/sessions/${sessionId}/messages`, request);
        return response.data;
    },
};