import { create } from 'zustand';

export const useAuthStore = create((set) => ({
  user: null,
  token: null,
  isAuthenticated: false,

  // Mock login action
  login: (userData, token) => set({ 
    user: userData, 
    token: token, 
    isAuthenticated: true 
  }),

  // Mock logout action
  logout: () => set({ 
    user: null, 
    token: null, 
    isAuthenticated: false 
  }),
}));
