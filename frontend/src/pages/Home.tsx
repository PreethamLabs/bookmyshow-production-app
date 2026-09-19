import { useState, useEffect, useMemo } from 'react';
import { Link, useSearchParams } from 'react-router-dom';
import { Clock, Calendar, LockKeyhole } from 'lucide-react';
import { movieApi, genreApi, movieGenreApi } from '../api/movies';
import { showApi } from '../api/shows';
import { theatreApi, screenApi } from '../api/theatres';
import type {
  MovieResponse,
  GenreResponse,
  MovieGenreResponse,
  ShowResponse,
  TheatreResponse,
  ScreenResponse,
} from '../types';
import { MovieStatus } from '../types';
import { Skeleton, EmptyState, ErrorState } from '../components/ui/Shared';
import { Modal } from '../components/ui/Modal';
import { useAuth } from '../context/AuthContext';
import { useCity } from '../context/CityContext';
import './Home.css';

export default function Home() {
  const { isAuthenticated } = useAuth();
  const { selectedCity } = useCity();
  const [movies, setMovies] = useState<MovieResponse[]>([]);
  const [genres, setGenres] = useState<GenreResponse[]>([]);
  const [movieGenres, setMovieGenres] = useState<MovieGenreResponse[]>([]);
  const [shows, setShows] = useState<ShowResponse[]>([]);
  const [theatres, setTheatres] = useState<TheatreResponse[]>([]);
  const [screens, setScreens] = useState<ScreenResponse[]>([]);
  const [selectedGenre, setSelectedGenre] = useState<number | null>(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(false);
  const [showAuthPrompt, setShowAuthPrompt] = useState(false);
  const [searchParams] = useSearchParams();
  const searchQuery = searchParams.get('search') || '';

  useEffect(() => {
    const load = async () => {
      try {
        const m = await movieApi.getAll();
        setMovies(m);
        if (isAuthenticated) {
          const [g, mg, s, t, sc] = await Promise.all([
            genreApi.getAll(),
            movieGenreApi.getAll(),
            showApi.getAll(),
            theatreApi.getAll(),
            screenApi.getAll(),
          ]);
          setGenres(g);
          setMovieGenres(mg);
          setShows(s);
          setTheatres(t);
          setScreens(sc);
        }
      } catch {
        setError(true);
      } finally {
        setLoading(false);
      }
    };
    load();
  }, [isAuthenticated]);

  const genreMap = useMemo(() => {
    const map = new Map<number, number[]>();
    movieGenres.forEach(mg => {
      const arr = map.get(mg.movieId) || [];
      arr.push(mg.genreId);
      map.set(mg.movieId, arr);
    });
    return map;
  }, [movieGenres]);

  const genreNameMap = useMemo(() => {
    const map = new Map<number, string>();
    genres.forEach(g => map.set(g.id, g.name));
    return map;
  }, [genres]);

  const cityMovieIds = useMemo(() => {
    if (!selectedCity) return null;

    const cityTheatreIds = new Set(
      theatres.filter(theatre => theatre.cityId === selectedCity.id).map(theatre => theatre.id),
    );
    const cityScreenIds = new Set(
      screens.filter(screen => cityTheatreIds.has(screen.theatreId)).map(screen => screen.id),
    );

    return new Set(
      shows
        .filter(show => cityScreenIds.has(show.screenId))
        .map(show => show.movieId),
    );
  }, [selectedCity, shows, theatres, screens]);

  const filteredMovies = useMemo(() => {
    let result = movies.filter(
      movie =>
        movie.movieStatus === MovieStatus.RELEASED &&
        (cityMovieIds === null || cityMovieIds.has(movie.id)),
    );

    if (searchQuery) {
      const q = searchQuery.toLowerCase();
      result = result.filter(m => m.title.toLowerCase().includes(q));
    }

    if (selectedGenre) {
      result = result.filter(m => {
        const gIds = genreMap.get(m.id) || [];
        return gIds.includes(selectedGenre);
      });
    }

    return result;
  }, [movies, selectedGenre, searchQuery, genreMap, cityMovieIds]);

  const publicMovies = useMemo(() => {
    const query = searchQuery.trim().toLowerCase();
    return movies
      .filter(movie =>
        movie.movieStatus === MovieStatus.RELEASED &&
        (!query || movie.title.toLowerCase().includes(query)),
      )
      .slice(0, 6);
  }, [movies, searchQuery]);

  const formatDuration = (mins: number) => {
    const h = Math.floor(mins / 60);
    const m = mins % 60;
    return `${h}h ${m}m`;
  };

  if (!isAuthenticated) {
    return (
      <>
        <section className="cv-home-hero">
          <div className="container cv-home-hero-content">
            <h1>
              Your next <span>cinematic experience</span> awaits
            </h1>
            <p className="cv-home-hero-desc">
              Discover the latest movies, find the best theatres near you, and book your tickets in seconds with Marquee.
            </p>
            <div style={{ marginTop: 'var(--space-8)', display: 'flex', gap: 'var(--space-3)' }}>
              <Link to="/login" className="cv-btn cv-btn--primary cv-btn--lg">Get Started</Link>
              <Link to="/register" className="cv-btn cv-btn--secondary cv-btn--lg">Create Account</Link>
            </div>
          </div>
        </section>
        <section className="container cv-public-movies">
          <div className="cv-public-movies-header">
            <div>
              <p className="cv-public-movies-kicker">A first look</p>
              <h2 className="cv-section-title">Explore movies</h2>
              <p className="cv-public-movies-desc">Browse what is playing before you choose your city.</p>
            </div>
            <button className="cv-btn cv-btn--secondary cv-btn--sm" onClick={() => setShowAuthPrompt(true)}>
              See more
            </button>
          </div>
          {loading ? (
            <div className="cv-movie-grid-skeleton">
              {Array.from({ length: 6 }).map((_, i) => <Skeleton key={i} variant="card" />)}
            </div>
          ) : error ? (
            <ErrorState onRetry={() => window.location.reload()} />
          ) : publicMovies.length === 0 ? (
            <EmptyState title="No movies found" description={searchQuery ? `No results for "${searchQuery}"` : 'Check back soon for new releases.'} />
          ) : (
            <div className="cv-movie-grid cv-public-movie-grid">
              {publicMovies.map(movie => (
                <button
                  key={movie.id}
                  className="cv-public-movie-card"
                  onClick={() => setShowAuthPrompt(true)}
                  aria-label={`Sign in to view ${movie.title}`}
                >
                  <div className="cv-movie-card-poster">
                    <img
                      src={movie.posterUrl}
                      alt={movie.title}
                      loading="lazy"
                      onError={e => {
                        (e.target as HTMLImageElement).src = `https://placehold.co/300x450/1a1a24/a78bfa?text=${encodeURIComponent(movie.title.slice(0, 12))}`;
                      }}
                    />
                    <span className="cv-public-movie-lock"><LockKeyhole size={14} /> Sign in to view</span>
                  </div>
                  <div className="cv-movie-card-info">
                    <h3 className="cv-movie-card-title">{movie.title}</h3>
                    <div className="cv-movie-card-meta"><Calendar size={12} /> {new Date(movie.releaseDate).toLocaleDateString('en-IN', { year: 'numeric', month: 'short' })}</div>
                  </div>
                </button>
              ))}
            </div>
          )}
        </section>
        <Modal isOpen={showAuthPrompt} onClose={() => setShowAuthPrompt(false)} title="Start your movie journey">
          <div className="cv-auth-prompt">
            <div className="cv-auth-prompt-icon"><LockKeyhole size={22} /></div>
            <p>Sign in or create an account to view movie details, choose a city, and book your seats.</p>
            <div className="cv-auth-prompt-actions">
              <Link to="/login" className="cv-btn cv-btn--primary cv-btn--full">Sign In</Link>
              <Link to="/register" className="cv-btn cv-btn--secondary cv-btn--full">Create Account</Link>
            </div>
          </div>
        </Modal>
      </>
    );
  }

  return (
    <>
      <section className="cv-home-hero">
        <div className="container cv-home-hero-content">
          <h1>
            Now <span>Showing</span>
          </h1>
          <p className="cv-home-hero-desc">
            Browse the latest releases and book your seats at the best theatres in your city.
          </p>
        </div>
      </section>

      <section className="container">
        <div className="cv-genre-filters">
          <button
            className={`cv-genre-chip ${!selectedGenre ? 'cv-genre-chip--active' : ''}`}
            onClick={() => setSelectedGenre(null)}
          >
            All
          </button>
          {genres.map(genre => (
            <button
              key={genre.id}
              className={`cv-genre-chip ${selectedGenre === genre.id ? 'cv-genre-chip--active' : ''}`}
              onClick={() => setSelectedGenre(genre.id === selectedGenre ? null : genre.id)}
            >
              {genre.name}
            </button>
          ))}
        </div>

        {loading ? (
          <div className="cv-movie-grid-skeleton">
            {Array.from({ length: 8 }).map((_, i) => (
              <div key={i} className="cv-movie-skeleton-card">
                <Skeleton variant="card" />
              </div>
            ))}
          </div>
        ) : error ? (
          <ErrorState onRetry={() => window.location.reload()} />
        ) : filteredMovies.length === 0 ? (
          <EmptyState
            title="No movies found"
            description={searchQuery ? `No results for "${searchQuery}"` : 'No movies match the selected genre.'}
          />
        ) : (
          <div className="cv-movie-grid">
            {filteredMovies.map((movie, idx) => (
              <Link
                to={`/movies/${movie.id}`}
                key={movie.id}
                className="cv-movie-card"
                style={{ animationDelay: `${idx * 40}ms`, animation: 'fadeInUp 0.4s ease both' }}
              >
                <div className="cv-movie-card-poster">
                  <img
                    src={movie.posterUrl}
                    alt={movie.title}
                    loading="lazy"
                    onError={(e) => {
                      (e.target as HTMLImageElement).src = `https://placehold.co/300x450/1a1a24/a78bfa?text=${encodeURIComponent(movie.title.slice(0, 12))}`;
                    }}
                  />
                  <div className="cv-movie-card-overlay">
                    <span className="cv-movie-card-overlay-text">
                      <Clock size={12} />
                      {formatDuration(movie.durationInMinutes)}
                    </span>
                  </div>
                </div>
                <div className="cv-movie-card-info">
                  <h3 className="cv-movie-card-title">{movie.title}</h3>
                  <div className="cv-movie-card-meta">
                    <Calendar size={12} />
                    {new Date(movie.releaseDate).toLocaleDateString('en-IN', { year: 'numeric', month: 'short', day: 'numeric' })}
                  </div>
                  <div className="cv-movie-card-genres">
                    {(genreMap.get(movie.id) || []).slice(0, 3).map(gid => (
                      <span key={gid} className="cv-movie-card-genre-tag">
                        {genreNameMap.get(gid)}
                      </span>
                    ))}
                  </div>
                </div>
              </Link>
            ))}
          </div>
        )}
      </section>
    </>
  );
}
