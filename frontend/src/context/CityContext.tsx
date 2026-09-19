import { createContext, useContext, useState, useEffect, useCallback, type ReactNode } from 'react';
import type { CityResponse } from '../types';
import { cityApi } from '../api/cities';
import { useAuth } from './AuthContext';

const DEFAULT_CITIES: CityResponse[] = [
  { id: 1, name: 'Bengaluru', state: 'Karnataka', country: 'India' },
  { id: 2, name: 'Mumbai', state: 'Maharashtra', country: 'India' },
  { id: 3, name: 'Hyderabad', state: 'Telangana', country: 'India' },
  { id: 4, name: 'Chennai', state: 'Tamil Nadu', country: 'India' },
  { id: 5, name: 'Delhi', state: 'Delhi', country: 'India' },
  { id: 6, name: 'Pune', state: 'Maharashtra', country: 'India' },
  { id: 7, name: 'Kolkata', state: 'West Bengal', country: 'India' },
  { id: 8, name: 'Ahmedabad', state: 'Gujarat', country: 'India' },
  { id: 9, name: 'Kochi', state: 'Kerala', country: 'India' },
  { id: 10, name: 'Jaipur', state: 'Rajasthan', country: 'India' },
  { id: 11, name: 'Lucknow', state: 'Uttar Pradesh', country: 'India' },
  { id: 12, name: 'Chandigarh', state: 'Chandigarh', country: 'India' },
];

interface CityContextType {
  cities: CityResponse[];
  selectedCity: CityResponse | null;
  selectCity: (city: CityResponse) => void;
  refreshCities: () => Promise<void>;
  isLoading: boolean;
}

const CityContext = createContext<CityContextType | undefined>(undefined);

export function CityProvider({ children }: { children: ReactNode }) {
  const { token, isAuthenticated } = useAuth();
  const [cities, setCities] = useState<CityResponse[]>(DEFAULT_CITIES);
  const [selectedCity, setSelectedCity] = useState<CityResponse | null>(() => {
    const storedId = localStorage.getItem('cinevault_city');
    if (storedId) {
      const found = DEFAULT_CITIES.find(c => c.id === Number(storedId));
      if (found) return found;
    }
    return DEFAULT_CITIES[0];
  });
  const [isLoading, setIsLoading] = useState(true);

  const loadCities = useCallback(async () => {
    try {
      const data = await cityApi.getAll();
      if (data && data.length > 0) {
        setCities(data);
        const storedId = localStorage.getItem('cinevault_city');
        if (storedId) {
          const found = data.find(c => c.id === Number(storedId));
          if (found) {
            setSelectedCity(found);
          } else {
            setSelectedCity(data[0]);
          }
        } else if (!selectedCity) {
          setSelectedCity(data[0]);
        }
      }
    } catch {
      // If API fails or unauthenticated, default cities are already in state
    } finally {
      setIsLoading(false);
    }
  }, [selectedCity]);

  useEffect(() => {
    loadCities();
  }, [loadCities, token, isAuthenticated]);

  const selectCity = (city: CityResponse) => {
    setSelectedCity(city);
    localStorage.setItem('cinevault_city', String(city.id));
  };

  return (
    <CityContext.Provider value={{ cities, selectedCity, selectCity, refreshCities: loadCities, isLoading }}>
      {children}
    </CityContext.Provider>
  );
}

export function useCity(): CityContextType {
  const context = useContext(CityContext);
  if (!context) throw new Error('useCity must be used within CityProvider');
  return context;
}

