import { useState, useEffect, useMemo, useCallback } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { ArrowLeft } from 'lucide-react';
import { showApi, showSeatApi } from '../api/shows';
import { bookingApi } from '../api/bookings';
import { movieApi } from '../api/movies';
import { theatreApi, screenApi } from '../api/theatres';
import type { ShowResponse, ShowSeatResponse, MovieResponse, TheatreResponse, ScreenResponse } from '../types';
import { SeatType, SeatStatus } from '../types';
import { Spinner, ErrorState } from '../components/ui/Shared';
import { Button } from '../components/ui/Button';
import { useToast } from '../context/ToastContext';
import { ApiError } from '../api/client';
import './SeatSelection.css';

export default function SeatSelection() {
  const { showId } = useParams<{ showId: string }>();
  const navigate = useNavigate();
  const { addToast } = useToast();

  const [show, setShow] = useState<ShowResponse | null>(null);
  const [movie, setMovie] = useState<MovieResponse | null>(null);
  const [theatre, setTheatre] = useState<TheatreResponse | null>(null);
  const [screen, setScreen] = useState<ScreenResponse | null>(null);
  const [seats, setSeats] = useState<ShowSeatResponse[]>([]);
  const [selectedSeatIds, setSelectedSeatIds] = useState<Set<number>>(new Set());
  const [loading, setLoading] = useState(true);
  const [booking, setBooking] = useState(false);
  const [error, setError] = useState(false);

  useEffect(() => {
    const load = async () => {
      try {
        const s = await showApi.getById(Number(showId));
        setShow(s);

        const [m, seatData, allScreens, allTheatres] = await Promise.all([
          movieApi.getById(s.movieId),
          showSeatApi.getByShow(s.id),
          screenApi.getAll(),
          theatreApi.getAll(),
        ]);

        setMovie(m);
        setSeats(seatData);

        const scr = allScreens.find(sc => sc.id === s.screenId);
        setScreen(scr || null);
        if (scr) {
          const th = allTheatres.find(t => t.id === scr.theatreId);
          setTheatre(th || null);
        }
      } catch {
        setError(true);
      } finally {
        setLoading(false);
      }
    };
    load();
  }, [showId]);

  const toggleSeat = useCallback((seatId: number) => {
    setSelectedSeatIds(prev => {
      const next = new Set(prev);
      if (next.has(seatId)) {
        next.delete(seatId);
      } else {
        if (next.size >= 10) return prev; // max 10 seats
        next.add(seatId);
      }
      return next;
    });
  }, []);

  const { regularSeats, premierSeats, reclinerSeats } = useMemo(() => {
    const regular: ShowSeatResponse[] = [];
    const premier: ShowSeatResponse[] = [];
    const recliner: ShowSeatResponse[] = [];

    seats.forEach(s => {
      switch (s.seatType) {
        case SeatType.REGULAR: regular.push(s); break;
        case SeatType.PREMIER: premier.push(s); break;
        case SeatType.RECLINER: recliner.push(s); break;
      }
    });

    const sortSeats = (arr: ShowSeatResponse[]) =>
      arr.sort((a, b) => {
        const aNum = parseInt(a.seatNumber.replace(/\D/g, ''));
        const bNum = parseInt(b.seatNumber.replace(/\D/g, ''));
        return aNum - bNum;
      });

    return {
      regularSeats: sortSeats(regular),
      premierSeats: sortSeats(premier),
      reclinerSeats: sortSeats(recliner),
    };
  }, [seats]);

  const totalPrice = useMemo(() => {
    if (!show) return 0;
    // Base price * 1 for REGULAR, 1.5 for PREMIER, 2 for RECLINER
    let total = 0;
    selectedSeatIds.forEach(id => {
      const seat = seats.find(s => s.id === id);
      if (!seat) return;
      const base = show.ticketPrice;
      switch (seat.seatType) {
        case SeatType.REGULAR: total += base; break;
        case SeatType.PREMIER: total += Math.round(base * 1.5); break;
        case SeatType.RECLINER: total += base * 2; break;
      }
    });
    return total;
  }, [selectedSeatIds, show, seats]);

  const handleBook = async () => {
    if (selectedSeatIds.size === 0 || !show) return;
    setBooking(true);
    try {
      const response = await bookingApi.create({
        showId: show.id,
        showSeatIds: Array.from(selectedSeatIds),
      });
      addToast('success', 'Booking created! Proceed to payment.');
      navigate(`/bookings/${response.id}`);
    } catch (err) {
      const msg = err instanceof ApiError ? err.message : 'Booking failed. Please try again.';
      addToast('error', msg);
    } finally {
      setBooking(false);
    }
  };

  const formatTime = (time: string) => {
    const [h, m] = time.split(':').map(Number);
    const ampm = h >= 12 ? 'PM' : 'AM';
    const h12 = h % 12 || 12;
    return `${h12}:${String(m).padStart(2, '0')} ${ampm}`;
  };

  const renderSeatRow = (rowSeats: ShowSeatResponse[], rowSize: number, isRecliner = false) => {
    const rows: ShowSeatResponse[][] = [];
    for (let i = 0; i < rowSeats.length; i += rowSize) {
      rows.push(rowSeats.slice(i, i + rowSize));
    }

    return rows.map((row, ri) => (
      <div key={ri} className="cv-seat-row">
        <span className="cv-seat-row-label">{row[0]?.seatNumber?.charAt(0)}{ri + 1}</span>
        {row.map(seat => {
          const isSelected = selectedSeatIds.has(seat.id);
          const isBooked = seat.seatStatus === SeatStatus.BOOKED;

          return (
            <button
              key={seat.id}
              className={`cv-seat ${isRecliner ? 'cv-seat--recliner' : ''} ${
                isBooked ? 'cv-seat--booked' : isSelected ? 'cv-seat--selected' : 'cv-seat--available'
              }`}
              onClick={() => !isBooked && toggleSeat(seat.id)}
              disabled={isBooked}
              title={`${seat.seatNumber} — ${seat.seatType}`}
              aria-label={`Seat ${seat.seatNumber}, ${isBooked ? 'booked' : isSelected ? 'selected' : 'available'}`}
            >
              {seat.seatNumber.replace(/^[A-Z]/, '')}
            </button>
          );
        })}
        <span className="cv-seat-row-label">{row[0]?.seatNumber?.charAt(0)}{ri + 1}</span>
      </div>
    ));
  };

  if (loading) return <Spinner />;
  if (error || !show || !movie) return <ErrorState onRetry={() => window.location.reload()} />;

  return (
    <div className="cv-seat-page container">
      <div className="cv-seat-page-header">
        <div>
          <Button variant="ghost" size="sm" onClick={() => navigate(-1)} style={{ marginBottom: 8 }}>
            <ArrowLeft size={16} /> Back
          </Button>
          <h1 className="cv-seat-page-title">{movie.title}</h1>
          <p className="cv-seat-page-subtitle">
            {theatre?.name} • {screen?.name} •{' '}
            {new Date(show.showDate).toLocaleDateString('en-IN', { weekday: 'short', month: 'short', day: 'numeric' })}
            {' • '}{formatTime(show.startTime)}
          </p>
        </div>
      </div>

      <div className="cv-screen-indicator">
        <div className="cv-screen-curve" />
        <div className="cv-screen-label">Screen this way</div>
      </div>

      <div className="cv-seat-map">
        {regularSeats.length > 0 && (
          <div className="cv-seat-section">
            <div className="cv-seat-section-label">
              Regular — <span className="cv-seat-section-price">₹{show.ticketPrice}</span>
            </div>
            {renderSeatRow(regularSeats, 15)}
          </div>
        )}

        {premierSeats.length > 0 && (
          <div className="cv-seat-section">
            <div className="cv-seat-section-label">
              Premier — <span className="cv-seat-section-price">₹{Math.round(show.ticketPrice * 1.5)}</span>
            </div>
            {renderSeatRow(premierSeats, 10)}
          </div>
        )}

        {reclinerSeats.length > 0 && (
          <div className="cv-seat-section">
            <div className="cv-seat-section-label">
              Recliner — <span className="cv-seat-section-price">₹{show.ticketPrice * 2}</span>
            </div>
            {renderSeatRow(reclinerSeats, 10, true)}
          </div>
        )}
      </div>

      <div className="cv-seat-legend">
        <div className="cv-seat-legend-item">
          <div className="cv-seat-legend-swatch cv-seat-legend-swatch--available" />
          Available
        </div>
        <div className="cv-seat-legend-item">
          <div className="cv-seat-legend-swatch cv-seat-legend-swatch--selected" />
          Selected
        </div>
        <div className="cv-seat-legend-item">
          <div className="cv-seat-legend-swatch cv-seat-legend-swatch--booked" />
          Booked
        </div>
      </div>

      {selectedSeatIds.size > 0 && (
        <div className="cv-booking-bar">
          <div className="cv-booking-bar-inner container">
            <div className="cv-booking-bar-info">
              <span className="cv-booking-bar-seats">
                {selectedSeatIds.size} seat{selectedSeatIds.size > 1 ? 's' : ''} selected
              </span>
              <span className="cv-booking-bar-price">₹{totalPrice.toLocaleString('en-IN')}</span>
            </div>
            <Button onClick={handleBook} loading={booking}>
              Book Now
            </Button>
          </div>
        </div>
      )}
    </div>
  );
}
