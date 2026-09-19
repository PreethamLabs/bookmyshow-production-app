import { api } from './client';
import type {
  ShowResponse,
  ShowCreateRequest,
  ShowUpdateRequest,
  ShowSeatResponse,
} from '../types';

export const showApi = {
  getAll: () => api.get<ShowResponse[]>('/shows'),
  getById: (id: number) => api.get<ShowResponse>(`/shows/${id}`),
  create: (data: ShowCreateRequest) => api.post<ShowResponse>('/shows', data),
  update: (id: number, data: ShowUpdateRequest) => api.put<ShowResponse>(`/shows/${id}`, data),
  delete: (id: number) => api.delete<void>(`/shows/${id}`),
};

export const showSeatApi = {
  getByShow: (showId: number) => api.get<ShowSeatResponse[]>(`/show-seats/show/${showId}`),
  getById: (id: number) => api.get<ShowSeatResponse>(`/show-seats/${id}`),
  lock: (id: number) => api.put<ShowSeatResponse>(`/show-seats/${id}/lock`),
  release: (id: number) => api.put<ShowSeatResponse>(`/show-seats/${id}/release`),
  book: (id: number) => api.put<ShowSeatResponse>(`/show-seats/${id}/book`),
};
