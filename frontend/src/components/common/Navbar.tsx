import { useState, useRef, useEffect } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import {
  Search, MapPin, ChevronDown, Sun, Moon, User, LogOut,
  LayoutDashboard, Ticket, Film, Check,
} from 'lucide-react';
import { useAuth } from '../../context/AuthContext';
import { useTheme } from '../../context/ThemeContext';
import { useCity } from '../../context/CityContext';
import { Button } from '../ui/Button';
import './Navbar.css';

export function Navbar() {
  const { user, isAuthenticated, isAdmin, logout } = useAuth();
  const { theme, toggleTheme } = useTheme();
  const { cities, selectedCity, selectCity } = useCity();
  const navigate = useNavigate();
  const location = useLocation();

  const [showUserMenu, setShowUserMenu] = useState(false);
  const [showCityMenu, setShowCityMenu] = useState(false);
  const [citySearch, setCitySearch] = useState('');
  const [searchQuery, setSearchQuery] = useState('');

  const userMenuRef = useRef<HTMLDivElement>(null);
  const cityMenuRef = useRef<HTMLDivElement>(null);

  useEffect(() => {
    const handler = (e: MouseEvent) => {
      if (userMenuRef.current && !userMenuRef.current.contains(e.target as Node)) {
        setShowUserMenu(false);
      }
      if (cityMenuRef.current && !cityMenuRef.current.contains(e.target as Node)) {
        setShowCityMenu(false);
      }
    };
    document.addEventListener('mousedown', handler);
    return () => document.removeEventListener('mousedown', handler);
  }, []);

  useEffect(() => {
    setSearchQuery(new URLSearchParams(location.search).get('search') || '');
  }, [location.search]);

  const filteredCities = cities.filter(c =>
    c.name.toLowerCase().includes(citySearch.toLowerCase())
  );

  const handleSearch = (e: React.FormEvent) => {
    e.preventDefault();
    navigate(searchQuery.trim() ? `/?search=${encodeURIComponent(searchQuery.trim())}` : '/');
  };

  const handleSearchChange = (value: string) => {
    setSearchQuery(value);
    const query = value.trim();
    navigate(query ? `/?search=${encodeURIComponent(query)}` : '/');
  };

  const handleLogout = () => {
    logout();
    setShowUserMenu(false);
    navigate('/');
  };

  return (
    <nav className="cv-navbar">
      <div className="cv-navbar-inner container">
        <Link to="/" className="cv-navbar-logo">
          <span className="cv-navbar-logo-icon">
            <Film size={18} />
          </span>
          Marquee
        </Link>

        <div className="cv-city-selector" ref={cityMenuRef}>
          <button
            className="cv-city-btn"
            onClick={() => setShowCityMenu(!showCityMenu)}
            aria-label="Select city"
          >
            <MapPin size={14} />
            <span>{selectedCity?.name || 'Select City'}</span>
            <ChevronDown size={14} />
          </button>

          {showCityMenu && (
            <div className="cv-city-dropdown">
              <div className="cv-city-dropdown-search">
                <input
                  type="text"
                  placeholder="Search city..."
                  value={citySearch}
                  onChange={e => setCitySearch(e.target.value)}
                  autoFocus
                />
              </div>
              {filteredCities.length > 0 ? (
                filteredCities.map(city => (
                  <button
                    key={city.id}
                    className={`cv-city-option ${selectedCity?.id === city.id ? 'cv-city-option--active' : ''}`}
                    onClick={() => {
                      selectCity(city);
                      setShowCityMenu(false);
                      setCitySearch('');
                    }}
                  >
                    <span>{city.name}</span>
                    <span className="cv-city-option-state">
                      {selectedCity?.id === city.id ? <Check size={14} /> : city.state}
                    </span>
                  </button>
                ))
              ) : (
                <div style={{ padding: '12px', textAlign: 'center', color: 'var(--text-tertiary)', fontSize: 'var(--font-size-sm)' }}>
                  No cities found
                </div>
              )}
            </div>
          )}
        </div>

        <form className="cv-navbar-search" onSubmit={handleSearch}>
          <Search size={16} className="cv-navbar-search-icon" />
          <input
            type="text"
            className="cv-navbar-search-input"
            placeholder="Search movies, theatres..."
            value={searchQuery}
            onChange={e => handleSearchChange(e.target.value)}
          />
        </form>

        <div className="cv-navbar-actions">
          <button className="cv-theme-toggle" onClick={toggleTheme} aria-label="Toggle theme">
            {theme === 'dark' ? <Sun size={18} /> : <Moon size={18} />}
          </button>

          {isAuthenticated ? (
            <div className="cv-navbar-user-menu" ref={userMenuRef}>
              <button
                className="cv-navbar-user-btn"
                onClick={() => setShowUserMenu(!showUserMenu)}
              >
                <span className="cv-navbar-avatar">
                  {user?.email?.charAt(0).toUpperCase()}
                </span>
              </button>

              {showUserMenu && (
                <div className="cv-navbar-dropdown">
                  <div style={{ padding: '8px 12px', borderBottom: '1px solid var(--border-secondary)', marginBottom: '4px' }}>
                    <div style={{ fontSize: 'var(--font-size-sm)', fontWeight: 600, color: 'var(--text-primary)' }}>
                      {user?.email}
                    </div>
                    <div style={{ fontSize: 'var(--font-size-xs)', color: 'var(--text-tertiary)', marginTop: 2 }}>
                      {user?.role}
                    </div>
                  </div>

                  <Link to="/my-bookings" className="cv-navbar-dropdown-item" onClick={() => setShowUserMenu(false)}>
                    <Ticket size={16} /> My Bookings
                  </Link>
                  <Link to="/profile" className="cv-navbar-dropdown-item" onClick={() => setShowUserMenu(false)}>
                    <User size={16} /> Profile
                  </Link>

                  {isAdmin && (
                    <>
                      <div className="cv-navbar-dropdown-divider" />
                      <Link to="/admin" className="cv-navbar-dropdown-item" onClick={() => setShowUserMenu(false)}>
                        <LayoutDashboard size={16} /> Admin Panel
                      </Link>
                    </>
                  )}

                  <div className="cv-navbar-dropdown-divider" />
                  <button className="cv-navbar-dropdown-item cv-navbar-dropdown-item--danger" onClick={handleLogout}>
                    <LogOut size={16} /> Sign Out
                  </button>
                </div>
              )}
            </div>
          ) : (
            <div style={{ display: 'flex', gap: 'var(--space-2)' }}>
              <Button variant="ghost" size="sm" onClick={() => navigate('/login')}>
                Sign In
              </Button>
              <Button variant="primary" size="sm" onClick={() => navigate('/register')}>
                Sign Up
              </Button>
            </div>
          )}
        </div>
      </div>
    </nav>
  );
}
