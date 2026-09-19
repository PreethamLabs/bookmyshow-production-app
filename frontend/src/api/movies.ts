import { api } from './client';
import type {
  MovieResponse,
  MovieCreateRequest,
  MovieUpdateRequest,
  GenreResponse,
  LanguageResponse,
  PersonResponse,
  MovieGenreResponse,
  MovieLanguageResponse,
  MoviePersonResponse,
} from '../types';

export const movieApi = {
  getAll: () => api.get<MovieResponse[]>('/movies'),
  getById: (id: number) => api.get<MovieResponse>(`/movies/${id}`),
  create: (data: MovieCreateRequest) => api.post<MovieResponse>('/movies', data),
  update: (id: number, data: MovieUpdateRequest) => api.put<MovieResponse>(`/movies/${id}`, data),
  archive: (id: number) => api.delete<void>(`/movies/${id}`),
};

export const genreApi = {
  getAll: () => api.get<GenreResponse[]>('/genres'),
};

export const languageApi = {
  getAll: () => api.get<LanguageResponse[]>('/languages'),
};

export const personApi = {
  getAll: () => api.get<PersonResponse[]>('/persons'),
};

export const movieGenreApi = {
  getAll: () => api.get<MovieGenreResponse[]>('/movie-genres'),
};

export const movieLanguageApi = {
  getAll: () => api.get<MovieLanguageResponse[]>('/movie-languages'),
};

export const moviePersonApi = {
  getAll: () => api.get<MoviePersonResponse[]>('/movie-persons'),
};
