import { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import { Calendar, Clock, Ticket, CreditCard } from 'lucide-react';
import { bookingApi } from '../api/bookings';
import { showApi } from '../api/shows';
import { movieApi } from '../api/movies';
import type { BookingResponse, ShowResponse, MovieResponse } from '../types';
import { BookingStatus } from '../types';
import { Spinner, EmptyState, ErrorState, Badge } from '../components/ui/Shared';
import './MyBookings.css';

const statusBadge: Record<string, { variant: 'success' | 'danger' | 'warning' | 'info' | 'neutral'; label: string }> = {
  CONFIRMED: { variant: 'success', label: 'Confirmed' },
  CREATED: { variant: 'warning', label: 'Pending' },
  PENDING_PAYMENT: { variant: 'warning', label: 'Awaiting Payment' },
  CANCELLED: { variant: 'danger', label: 'Cancelled' },
  FAILED: { variant: 'danger', label: 'Failed' },
  EXPIRED: { variant: 'neutral', label: 'Expired' },
};

export default function MyBookings() {
  const [bookings, setBookings] = useState<BookingResponse[]>([]);
  const [shows, setShows] = useState<Map<number, ShowResponse>>(new Map());
  const [movies, setMovies] = useState<Map<number, MovieResponse>>(new Map());
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(false);

  useEffect(() => {
    const load = async () => {
      try {
        const b = await bookingApi.getMyBookings();
        setBookings(b.sort((a, c) => new Date(c.createdAt).getTime() - new Date(a.createdAt).getTime()));

        const [allShows, allMovies] = await Promise.all([
          showApi.getAll(),
          movieApi.getAll(),
        ]);

        const sMap = new Map<number, ShowResponse>();
        allShows.forEach(s => sMap.set(s.id, s));
        setShows(sMap);

        const mMap = new Map<number, MovieResponse>();
        allMovies.forEach(m => mMap.set(m.id, m));
        setMovies(mMap);
      } catch {
        setError(true);
      } finally {
        setLoading(false);
      }
    };
    load();
  }, []);

  const formatTime = (time: string) => {
    const [h, m] = time.split(':').map(Number);
    const ampm = h >= 12 ? 'PM' : 'AM';
    const h12 = h % 12 || 12;
    return `${h12}:${String(m).padStart(2, '0')} ${ampm}`;
  };

  if (loading) return <Spinner />;
  if (error) return <ErrorState onRetry={() => window.location.reload()} />;

  if (bookings.length === 0) {
    return (
      <div className="cv-my-bookings container">
        <div className="cv-my-bookings-header">
          <h1>My Bookings</h1>
        </div>
        <EmptyState
          icon={<Ticket size={28} />}
          title="No bookings yet"
          description="When you book a movie, your bookings will appear here."
          action={<Link to="/" className="cv-btn cv-btn--primary">Browse Movies</Link>}
        />
      </div>
    );
  }

  return (
    <div className="cv-my-bookings container">
      <div className="cv-my-bookings-header">
        <h1>My Bookings</h1>
      </div>

      <div className="cv-my-bookings-list">
        {bookings.map((b, idx) => {
          const show = shows.get(b.showId);
          const movie = show ? movies.get(show.movieId) : undefined;
          const cfg = statusBadge[b.bookingStatus] || statusBadge.CREATED;

          return (
            <Link
              to={`/bookings/${b.id}`}
              key={b.id}
              className="cv-booking-item"
              style={{ animationDelay: `${idx * 60}ms`, animation: 'fadeInUp 0.4s ease both' }}
            >
              <div className="cv-booking-item-poster">
                {movie && (
                  <img
                    src={movie.posterUrl}
                    alt={movie.title}
                    onError={(e) => {
                      (e.target as HTMLImageElement).src = `https://placehold.co/120x180/1a1a24/a78bfa?text=${encodeURIComponent((movie.title || '?').slice(0, 6))}`;
                    }}
                  />
                )}
              </div>
              <div className="cv-booking-item-content">
                <h3 className="cv-booking-item-title">{movie?.title || `Booking #${b.id}`}</h3>
                <div className="cv-booking-item-meta">
                  {show && (
                    <>
                      <span>
                        <Calendar size={12} />
                        {new Date(show.showDate).toLocaleDateString('en-IN', { month: 'short', day: 'numeric' })}
                      </span>
                      <span>
                        <Clock size={12} />
                        {formatTime(show.startTime)}
                      </span>
                    </>
                  )}
                  <span>
                    <CreditCard size={12} />
                    {new Date(b.createdAt).toLocaleDateString('en-IN')}
                  </span>
                </div>
              </div>
              <div className="cv-booking-item-right">
                <Badge variant={cfg.variant}>{cfg.label}</Badge>
                <span className="cv-booking-item-price">₹{b.totalPrice?.toLocaleString('en-IN') || '—'}</span>
              </div>
            </Link>
          );
        })}
      </div>
    </div>
  );
}
