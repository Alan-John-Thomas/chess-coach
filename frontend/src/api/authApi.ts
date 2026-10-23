import client from './client';
import { AuthResponse, LoginRequest, RegisterRequest, User } from '../types';

export const authApi = {
    login: async (credentials: LoginRequest): Promise<AuthResponse> => {
        const response = await client.post<AuthResponse>('/auth/login', credentials);
        return response.data;
    },

    register: async (data: RegisterRequest): Promise<User> => {
        const response = await client.post<User>('/auth/register', data);
        return response.data;
    },
};