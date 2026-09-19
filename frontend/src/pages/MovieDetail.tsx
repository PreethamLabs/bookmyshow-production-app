import { useState, useEffect, useMemo } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { Clock, Calendar, Globe, Star } from 'lucide-react';
import { movieApi, genreApi, languageApi, personApi, movieGenreApi, movieLanguageApi, moviePersonApi } from '../api/movies';
import { showApi } from '../api/shows';
import { theatreApi, screenApi } from '../api/theatres';
import { useCity } from '../context/CityContext';
import type {
  MovieResponse, GenreResponse, LanguageResponse, PersonResponse,
  MovieGenreResponse, MovieLanguageResponse, MoviePersonResponse,
  ShowResponse, TheatreResponse, ScreenResponse,
} from '../types';
import { Spinner, ErrorState } from '../components/ui/Shared';
import './MovieDetail.css';

export default function MovieDetail() {
  const { id } = useParams<{ id: string }>();
  const navigate = useNavigate();
  const { selectedCity } = useCity();

  const [movie, setMovie] = useState<MovieResponse | null>(null);
  const [genres, setGenres] = useState<GenreResponse[]>([]);
  const [languages, setLanguages] = useState<LanguageResponse[]>([]);
  const [persons, setPersons] = useState<PersonResponse[]>([]);
  const [movieGenres, setMovieGenres] = useState<MovieGenreResponse[]>([]);
  const [movieLanguages, setMovieLanguages] = useState<MovieLanguageResponse[]>([]);
  const [moviePersons, setMoviePersons] = useState<MoviePersonResponse[]>([]);
  const [shows, setShows] = useState<ShowResponse[]>([]);
  const [theatres, setTheatres] = useState<TheatreResponse[]>([]);
  const [screens, setScreens] = useState<ScreenResponse[]>([]);
  const [selectedDate, setSelectedDate] = useState<string>('');
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(false);

  useEffect(() => {
    const load = async () => {
      try {
        const [m, g, l, p, mg, ml, mp, s, t, sc] = await Promise.all([
          movieApi.getById(Number(id)),
          genreApi.getAll(),
          languageApi.getAll(),
          personApi.getAll(),
          movieGenreApi.getAll(),
          movieLanguageApi.getAll(),
          moviePersonApi.getAll(),
          showApi.getAll(),
          theatreApi.getAll(),
          screenApi.getAll(),
        ]);
        setMovie(m);
        setGenres(g);
        setLanguages(l);
        setPersons(p);
        setMovieGenres(mg);
        setMovieLanguages(ml);
        setMoviePersons(mp);
        setShows(s);
        setTheatres(t);
        setScreens(sc);
      } catch {
        setError(true);
      } finally {
        setLoading(false);
      }
    };
    load();
  }, [id]);

  const movieId = Number(id);

  const myGenres = useMemo(() => {
    const gIds = movieGenres.filter(mg => mg.movieId === movieId).map(mg => mg.genreId);
    return genres.filter(g => gIds.includes(g.id));
  }, [movieGenres, genres, movieId]);

  const myLanguages = useMemo(() => {
    const lIds = movieLanguages.filter(ml => ml.movieId === movieId).map(ml => ml.languageId);
    return languages.filter(l => lIds.includes(l.id));
  }, [movieLanguages, languages, movieId]);

  const myCast = useMemo(() => {
    return moviePersons
      .filter(mp => mp.movieId === movieId)
      .map(mp => ({
        person: persons.find(p => p.id === mp.personId),
        role: mp.role,
      }))
      .filter(c => c.person) as Array<{ person: PersonResponse; role: string }>;
  }, [moviePersons, persons, movieId]);

  // Shows for this movie in selected city
  const cityTheatres = useMemo(() => {
    if (!selectedCity) return [];
    return theatres.filter(t => t.cityId === selectedCity.id);
  }, [theatres, selectedCity]);

  const cityScreenIds = useMemo(() => {
    const tIds = new Set(cityTheatres.map(t => t.id));
    return screens.filter(s => tIds.has(s.theatreId)).map(s => s.id);
  }, [screens, cityTheatres]);

  const movieShows = useMemo(() => {
    const scrSet = new Set(cityScreenIds);
    return shows.filter(s => s.movieId === movieId && scrSet.has(s.screenId));
  }, [shows, movieId, cityScreenIds]);

  const uniqueDates = useMemo(() => {
    const dates = [...new Set(movieShows.map(s => s.showDate))].sort();
    return dates;
  }, [movieShows]);

  useEffect(() => {
    if (uniqueDates.length > 0 && !selectedDate) {
      setSelectedDate(uniqueDates[0]);
    }
  }, [uniqueDates, selectedDate]);

  const showsByTheatre = useMemo(() => {
    const screenMap = new Map<number, ScreenResponse>();
    screens.forEach(s => screenMap.set(s.id, s));

    const theatreMap = new Map<number, TheatreResponse>();
    cityTheatres.forEach(t => theatreMap.set(t.id, t));

    const dateShows = movieShows.filter(s => s.showDate === selectedDate);

    const groups = new Map<number, { theatre: TheatreResponse; shows: ShowResponse[] }>();

    dateShows.forEach(show => {
      const screen = screenMap.get(show.screenId);
      if (!screen) return;
      const theatre = theatreMap.get(screen.theatreId);
      if (!theatre) return;

      if (!groups.has(theatre.id)) {
        groups.set(theatre.id, { theatre, shows: [] });
      }
      groups.get(theatre.id)!.shows.push(show);
    });

    return Array.from(groups.values());
  }, [movieShows, selectedDate, screens, cityTheatres]);

  const formatTime = (time: string) => {
    const [h, m] = time.split(':').map(Number);
    const ampm = h >= 12 ? 'PM' : 'AM';
    const h12 = h % 12 || 12;
    return `${h12}:${String(m).padStart(2, '0')} ${ampm}`;
  };

  const formatDuration = (mins: number) => {
    const h = Math.floor(mins / 60);
    const m = mins % 60;
    return `${h}h ${m}m`;
  };

  if (loading) return <Spinner />;
  if (error || !movie) return <ErrorState onRetry={() => window.location.reload()} />;

  return (
    <div className="cv-movie-detail container">
      <div className="cv-movie-detail-hero">
        <div className="cv-movie-detail-poster">
          <img
            src={movie.posterUrl}
            alt={movie.title}
            onError={(e) => {
              (e.target as HTMLImageElement).src = `https://placehold.co/400x600/1a1a24/a78bfa?text=${encodeURIComponent(movie.title.slice(0, 12))}`;
            }}
          />
        </div>

        <div className="cv-movie-detail-info">
          <h1 className="cv-movie-detail-title">{movie.title}</h1>

          <div className="cv-movie-detail-meta">
            <span className="cv-movie-detail-meta-item">
              <Clock size={15} />
              {formatDuration(movie.durationInMinutes)}
            </span>
            <span className="cv-movie-detail-meta-item">
              <Calendar size={15} />
              {new Date(movie.releaseDate).toLocaleDateString('en-IN', { year: 'numeric', month: 'long', day: 'numeric' })}
            </span>
            {myLanguages.length > 0 && (
              <span className="cv-movie-detail-meta-item">
                <Globe size={15} />
                {myLanguages.map(l => l.name).join(', ')}
              </span>
            )}
          </div>

          <div className="cv-movie-detail-tags">
            {myGenres.map(g => (
              <span key={g.id} className="cv-movie-detail-tag">{g.name}</span>
            ))}
          </div>

          <p className="cv-movie-detail-desc">{movie.description}</p>

          {myCast.length > 0 && (
            <div className="cv-movie-cast">
              <h3>Cast & Crew</h3>
              <div className="cv-movie-cast-list">
                {myCast.map((c, i) => (
                  <div key={i} className="cv-movie-cast-item">
                    <Star size={12} />
                    <span>{c.person.name}</span>
                    <span className="cv-movie-cast-role">({c.role})</span>
                  </div>
                ))}
              </div>
            </div>
          )}
        </div>
      </div>

      <section className="cv-shows-section">
        <div className="cv-section-header">
          <h2 className="cv-section-title">
            Shows in {selectedCity?.name || 'your city'}
          </h2>
        </div>

        {uniqueDates.length === 0 ? (
          <div style={{ padding: 'var(--space-8) 0', textAlign: 'center', color: 'var(--text-tertiary)' }}>
            No shows available in {selectedCity?.name || 'this city'} right now.
          </div>
        ) : (
          <>
            <div className="cv-shows-date-picker">
              {uniqueDates.map(date => {
                const d = new Date(date);
                const isActive = selectedDate === date;
                return (
                  <button
                    key={date}
                    className={`cv-date-chip ${isActive ? 'cv-date-chip--active' : ''}`}
                    onClick={() => setSelectedDate(date)}
                  >
                    <span className="cv-date-chip-day">
                      {d.toLocaleDateString('en-IN', { weekday: 'short' })}
                    </span>
                    <span className="cv-date-chip-date">{d.getDate()}</span>
                    <span className="cv-date-chip-month">
                      {d.toLocaleDateString('en-IN', { month: 'short' })}
                    </span>
                  </button>
                );
              })}
            </div>

            {showsByTheatre.length === 0 ? (
              <div style={{ padding: 'var(--space-6) 0', textAlign: 'center', color: 'var(--text-tertiary)' }}>
                No shows on this date.
              </div>
            ) : (
              showsByTheatre.map(({ theatre, shows: theatreShows }) => (
                <div key={theatre.id} className="cv-theatre-group">
                  <div className="cv-theatre-group-header">
                    <div>
                      <div className="cv-theatre-group-name">{theatre.name}</div>
                      <div className="cv-theatre-group-address">{theatre.address}</div>
                    </div>
                  </div>
                  <div className="cv-theatre-group-shows">
                    {theatreShows
                      .sort((a, b) => a.startTime.localeCompare(b.startTime))
                      .map(show => (
                        <button
                          key={show.id}
                          className="cv-show-time-btn"
                          onClick={() => navigate(`/shows/${show.id}/seats`)}
                        >
                          {formatTime(show.startTime)}
                          <span className="cv-show-time-price">₹{show.ticketPrice}</span>
                        </button>
                      ))}
                  </div>
                </div>
              ))
            )}
          </>
        )}
      </section>
    </div>
  );
}
