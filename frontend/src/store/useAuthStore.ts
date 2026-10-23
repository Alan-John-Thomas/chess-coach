import { create } from 'zustand';
import { User } from '../types';

// structure of the store
export interface AuthState {
    user: User | null;
    token: string | null;
    isAuthenticated: boolean;
    login: (userData: User, token: string) => void;
    logout: () => void;
}

// 1. Initial hydration: read stored credentials from browser storage on startup
const storedToken = localStorage.getItem('chess_coach_token');
const storedUser = localStorage.getItem('chess_coach_user');

export const useAuthStore = create<AuthState>((set) => ({
    // when useAuthStore is called it update the value of the keys in the store as below (updates other keys)
    token: storedToken,
    user: storedUser ? JSON.parse(storedUser) : null,
    isAuthenticated: !!storedToken,

    // login is a key with a fn as value and fn logic as given below
    // 2. On login: persist to localStorage and update Zustand memory
    login: (userData: User, token: string) => {
        localStorage.setItem('chess_coach_token', token);
        localStorage.setItem('chess_coach_user', JSON.stringify(userData));
        set({
            user: userData,
            token,
            isAuthenticated: true,
        });
    },

    // logout is a key with a fn as value and fn logout as given below (updates other keys)
    // 3. On logout: purge localStorage and reset Zustand memory
    logout: () => {
        localStorage.removeItem('chess_coach_token');
        localStorage.removeItem('chess_coach_user');
        set({
            user: null,
            token: null,
            isAuthenticated: false,
        });
    },
}));