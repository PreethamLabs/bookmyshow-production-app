import { api } from './client';
import type {
  TheatreResponse,
  TheatreCreateRequest,
  TheatreUpdateRequest,
  ScreenResponse,
  ScreenCreateRequest,
} from '../types';

export const theatreApi = {
  getAll: () => api.get<TheatreResponse[]>('/theatres'),
  getById: (id: number) => api.get<TheatreResponse>(`/theatres/${id}`),
  create: (data: TheatreCreateRequest) => api.post<TheatreResponse>('/theatres', data),
  update: (id: number, data: TheatreUpdateRequest) => api.put<TheatreResponse>(`/theatres/${id}`, data),
};

export const screenApi = {
  getAll: () => api.get<ScreenResponse[]>('/screens'),
  getById: (id: number) => api.get<ScreenResponse>(`/screens/${id}`),
  create: (data: ScreenCreateRequest) => api.post<ScreenResponse>('/screens', data),
};
