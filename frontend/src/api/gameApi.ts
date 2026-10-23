import client from './client';
import { GameDetailDto, GameSummaryDto, GameUploadResponse } from '../types';

export const gameApi = {
    // 1. Upload a PGN file (multipart/form-data)
    uploadGame: async (file: File): Promise<GameUploadResponse> => {
        const formData = new FormData();
        formData.append('file', file);

        const response = await client.post<GameUploadResponse>('/games/upload', formData, {
            headers: {
                'Content-Type': 'multipart/form-data',
            },
        });
        return response.data;
    },

    // 2. Fetch list of all games for the authenticated user
    getUserGames: async (): Promise<GameSummaryDto[]> => {
        const response = await client.get<GameSummaryDto[]>('/games');
        return response.data;
    },

    // 3. Fetch full game detail (moves, FENs, PGN text) by ID
    getGameDetail: async (gameId: string): Promise<GameDetailDto> => {
        const response = await client.get<GameDetailDto>(`/games/${gameId}`);
        return response.data;
    },
};