import { createContext, useContext, useState, useEffect, useCallback, type ReactNode } from 'react';
import type { Role, JwtPayload } from '../types';
import { authApi } from '../api/auth';
import type { LoginRequest, UserCreateRequest } from '../types';

interface AuthUser {
  email: string;
  role: Role;
}

interface AuthContextType {
  user: AuthUser | null;
  token: string | null;
  isAuthenticated: boolean;
  isAdmin: boolean;
  isLoading: boolean;
  login: (data: LoginRequest) => Promise<void>;
  register: (data: UserCreateRequest) => Promise<void>;
  logout: () => void;
}

const AuthContext = createContext<AuthContextType | undefined>(undefined);

function decodeJwt(token: string): JwtPayload | null {
  try {
    const payload = token.split('.')[1];
    const decoded = atob(payload);
    return JSON.parse(decoded) as JwtPayload;
  } catch {
    return null;
  }
}

function isTokenExpired(payload: JwtPayload): boolean {
  return Date.now() >= payload.exp * 1000;
}

export function AuthProvider({ children }: { children: ReactNode }) {
  const [user, setUser] = useState<AuthUser | null>(null);
  const [token, setToken] = useState<string | null>(null);
  const [isLoading, setIsLoading] = useState(true);

  const clearAuth = useCallback(() => {
    setUser(null);
    setToken(null);
    localStorage.removeItem('cinevault_token');
  }, []);

  const setAuth = useCallback((jwt: string) => {
    const payload = decodeJwt(jwt);
    if (!payload || isTokenExpired(payload)) {
      clearAuth();
      return false;
    }
    setToken(jwt);
    setUser({ email: payload.sub, role: payload.role });
    localStorage.setItem('cinevault_token', jwt);
    return true;
  }, [clearAuth]);

  useEffect(() => {
    const stored = localStorage.getItem('cinevault_token');
    if (stored) {
      setAuth(stored);
    }
    setIsLoading(false);
  }, [setAuth]);

  const login = async (data: LoginRequest) => {
    const response = await authApi.login(data);
    const ok = setAuth(response.token);
    if (!ok) throw new Error('Invalid or expired token received');
  };

  const register = async (data: UserCreateRequest) => {
    await authApi.register(data);
  };

  const logout = () => {
    clearAuth();
  };

  const value: AuthContextType = {
    user,
    token,
    isAuthenticated: !!user,
    isAdmin: user?.role === 'ADMIN',
    isLoading,
    login,
    register,
    logout,
  };

  return (
    <AuthContext.Provider value={value}>
      {children}
    </AuthContext.Provider>
  );
}

export function useAuth(): AuthContextType {
  const context = useContext(AuthContext);
  if (!context) {
    throw new Error('useAuth must be used within AuthProvider');
  }
  return context;
}
