import { api } from './client';
import type { CityResponse, CityCreateRequest } from '../types';

export const cityApi = {
  getAll: () => api.get<CityResponse[]>('/cities'),
  getById: (id: number) => api.get<CityResponse>(`/cities/${id}`),
  create: (data: CityCreateRequest) => api.post<CityResponse>('/cities', data),
};
