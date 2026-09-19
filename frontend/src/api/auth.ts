import { api } from './client';
import type { LoginRequest, LoginResponse, UserCreateRequest, UserResponse } from '../types';

export const authApi = {
  login: (data: LoginRequest) =>
    api.post<LoginResponse>('/auth/login', data),

  register: (data: UserCreateRequest) =>
    api.post<UserResponse>('/users', data),

  sendVerification: (email: string) =>
    api.post<void>('/users/send-verification', { email }),

  verifyEmail: (email: string, otp: string) =>
    api.post<void>('/users/verify-email', { email, otp }),

  deleteAccount: () =>
    api.delete<void>('/users/me'),
};
