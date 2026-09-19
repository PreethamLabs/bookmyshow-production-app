import { useState, useEffect } from 'react';
import QRCode from 'qrcode';
import { useParams, useNavigate } from 'react-router-dom';
import { CheckCircle, Clock, XCircle, CreditCard, ArrowLeft, Home, CalendarDays, MapPin, Ticket as TicketIcon } from 'lucide-react';
import { bookingApi, bookingSeatApi, ticketApi } from '../api/bookings';
import { showApi } from '../api/shows';
import { movieApi } from '../api/movies';
import { theatreApi, screenApi } from '../api/theatres';
import type { BookingResponse, BookingSeatResponse, TicketResponse, ShowResponse, MovieResponse, TheatreResponse, ScreenResponse } from '../types';
import { BookingStatus } from '../types';
import { Spinner, ErrorState, Badge } from '../components/ui/Shared';
import { Button } from '../components/ui/Button';
import { useToast } from '../context/ToastContext';
import { ApiError } from '../api/client';
import './BookingConfirmation.css';

interface RazorpayResponse {
  razorpay_order_id: string;
  razorpay_payment_id: string;
  razorpay_signature: string;
}

const statusConfig: Record<string, { icon: typeof CheckCircle; variant: 'success' | 'warning' | 'danger' | 'info' | 'neutral'; label: string }> = {
  CONFIRMED: { icon: CheckCircle, variant: 'success', label: 'Confirmed' },
  CREATED: { icon: Clock, variant: 'warning', label: 'Pending Payment' },
  PENDING_PAYMENT: { icon: CreditCard, variant: 'warning', label: 'Awaiting Payment' },
  CANCELLED: { icon: XCircle, variant: 'danger', label: 'Cancelled' },
  FAILED: { icon: XCircle, variant: 'danger', label: 'Failed' },
  EXPIRED: { icon: Clock, variant: 'neutral', label: 'Expired' },
};

export default function BookingConfirmation() {
  const { id } = useParams<{ id: string }>();
  const navigate = useNavigate();
  const { addToast } = useToast();

  const [booking, setBooking] = useState<BookingResponse | null>(null);
  const [seats, setSeats] = useState<BookingSeatResponse[]>([]);
  const [ticket, setTicket] = useState<TicketResponse | null>(null);
  const [show, setShow] = useState<ShowResponse | null>(null);
  const [movie, setMovie] = useState<MovieResponse | null>(null);
  const [theatre, setTheatre] = useState<TheatreResponse | null>(null);
  const [screen, setScreen] = useState<ScreenResponse | null>(null);
  const [loading, setLoading] = useState(true);
  const [paymentLoading, setPaymentLoading] = useState(false);
  const [error, setError] = useState(false);
  const [qrCodeUrl, setQrCodeUrl] = useState('');

  const loadBooking = async () => {
    try {
      const b = await bookingApi.getById(Number(id));
      setBooking(b);

      const [s, bs] = await Promise.all([
        showApi.getById(b.showId),
        bookingSeatApi.getByBooking(b.id),
      ]);
      setSeats(bs);
      setShow(s);

      const [m, allScreens, allTheatres] = await Promise.all([
        movieApi.getById(s.movieId),
        screenApi.getAll(),
        theatreApi.getAll(),
      ]);
      setMovie(m);
      const scr = allScreens.find(sc => sc.id === s.screenId);
      setScreen(scr || null);
      if (scr) {
        const th = allTheatres.find(t => t.id === scr.theatreId);
        setTheatre(th || null);
      }

      if (b.bookingStatus === BookingStatus.CONFIRMED) {
        try {
          const t = await ticketApi.getByBooking(b.id);
          setTicket(t);
        } catch { /* no ticket yet */ }
      }
    } catch {
      setError(true);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => { loadBooking(); }, [id]);

  useEffect(() => {
    if (!ticket || !booking) {
      setQrCodeUrl('');
      return;
    }

    const ticketDetails = [
      'Marquee movie ticket',
      `Ticket: ${ticket.ticketNumber}`,
      `Booking: #${booking.id}`,
      `Movie: ${movie?.title || 'Unavailable'}`,
      `Theatre: ${theatre?.name || 'Unavailable'}`,
      `Screen: ${screen?.name || 'Unavailable'}`,
      `Date: ${show?.showDate || 'Unavailable'}`,
      `Time: ${show?.startTime || 'Unavailable'}`,
      `Seats: ${seats.map(seat => seat.seatNumber).join(', ') || 'Unavailable'}`,
      `Total: INR ${booking.totalPrice}`,
    ].join('\n');

    QRCode.toDataURL(ticketDetails, {
      width: 144,
      margin: 1,
      errorCorrectionLevel: 'M',
      color: { dark: '#1b1533', light: '#ffffff' },
    })
      .then(setQrCodeUrl)
      .catch(() => setQrCodeUrl(''));
  }, [booking, movie, screen, seats, show, theatre, ticket]);

  const handlePayment = async () => {
    if (!booking) return;
    setPaymentLoading(true);
    try {
      const payment = await bookingApi.createPayment(booking.id);
      const razorpayKey = import.meta.env.VITE_RAZORPAY_KEY_ID;

      if (!razorpayKey) {
        throw new Error('Payment is not configured. Set VITE_RAZORPAY_KEY_ID in the frontend environment.');
      }

      if (payment.razorpayOrderId && (window as any).Razorpay) {
        const options = {
          key: razorpayKey,
          amount: payment.amount * 100,
          currency: 'INR',
          name: 'Marquee',
          description: `Booking #${booking.id}`,
          order_id: payment.razorpayOrderId,
          handler: async (response: RazorpayResponse) => {
            try {
              await bookingApi.verifyPayment({
                razorpayOrderId: response.razorpay_order_id,
                razorpayPaymentId: response.razorpay_payment_id,
                razorpaySignature: response.razorpay_signature,
              });
              addToast('success', 'Payment successful! Your tickets are confirmed.');
              await loadBooking();
            } catch (err) {
              const msg = err instanceof ApiError ? err.message : 'Payment verification failed.';
              addToast('error', msg);
            }
          },
          prefill: {},
          theme: { color: '#8b5cf6' },
        };
        const rzp = new (window as any).Razorpay(options);
        rzp.open();
      } else {
        addToast('info', 'Payment initiated. Refreshing...');
        setTimeout(() => loadBooking(), 2000);
      }
    } catch (err) {
      const msg = err instanceof ApiError ? err.message : 'Payment initiation failed.';
      addToast('error', msg);
    } finally {
      setPaymentLoading(false);
    }
  };

  const handleCancel = async () => {
    if (!booking || !confirm('Cancel this booking?')) return;
    try {
      await bookingApi.cancel(booking.id);
      addToast('info', 'Booking cancelled.');
      loadBooking();
    } catch (err) {
      const msg = err instanceof ApiError ? err.message : 'Cancellation failed.';
      addToast('error', msg);
    }
  };

  const formatTime = (time: string) => {
    const [h, m] = time.split(':').map(Number);
    const ampm = h >= 12 ? 'PM' : 'AM';
    const h12 = h % 12 || 12;
    return `${h12}:${String(m).padStart(2, '0')} ${ampm}`;
  };

  if (loading) return <Spinner />;
  if (error || !booking) return <ErrorState onRetry={() => window.location.reload()} />;

  const cfg = statusConfig[booking.bookingStatus] || statusConfig.CREATED;
  const StatusIcon = cfg.icon;
  const canPay = [BookingStatus.CREATED, BookingStatus.PENDING_PAYMENT].includes(booking.bookingStatus);
  const canCancel = [BookingStatus.CREATED, BookingStatus.PENDING_PAYMENT, BookingStatus.CONFIRMED].includes(booking.bookingStatus);

  return (
    <div className="cv-booking-confirm container">
      <Button variant="ghost" size="sm" onClick={() => navigate(-1)} style={{ marginBottom: 16 }}>
        <ArrowLeft size={16} /> Back
      </Button>

      <div className="cv-booking-confirm-card animate-scale-in">
        <div className="cv-booking-confirm-header">
          <div className={`cv-booking-confirm-status-icon cv-booking-confirm-status-icon--${cfg.variant === 'warning' ? 'pending' : cfg.variant === 'danger' ? 'error' : 'success'}`}>
            <StatusIcon size={28} />
          </div>
          <h1 className="cv-booking-confirm-title">
            {booking.bookingStatus === BookingStatus.CONFIRMED ? 'Booking Confirmed!' : cfg.label}
          </h1>
          <p className="cv-booking-confirm-subtitle">Booking #{booking.id}</p>
        </div>

        <div className="cv-booking-confirm-body">
          {movie && (
            <div className="cv-booking-detail-row">
              <span className="cv-booking-detail-label">Movie</span>
              <span className="cv-booking-detail-value">{movie.title}</span>
            </div>
          )}
          {theatre && (
            <div className="cv-booking-detail-row">
              <span className="cv-booking-detail-label">Theatre</span>
              <span className="cv-booking-detail-value">{theatre.name}</span>
            </div>
          )}
          {show && (
            <>
              <div className="cv-booking-detail-row">
                <span className="cv-booking-detail-label">Date</span>
                <span className="cv-booking-detail-value">
                  {new Date(show.showDate).toLocaleDateString('en-IN', { weekday: 'short', month: 'long', day: 'numeric', year: 'numeric' })}
                </span>
              </div>
              <div className="cv-booking-detail-row">
                <span className="cv-booking-detail-label">Time</span>
                <span className="cv-booking-detail-value">{formatTime(show.startTime)}</span>
              </div>
            </>
          )}
          {screen && (
            <div className="cv-booking-detail-row">
              <span className="cv-booking-detail-label">Screen</span>
              <span className="cv-booking-detail-value">{screen.name}</span>
            </div>
          )}
          <div className="cv-booking-detail-row cv-booking-detail-row--seats">
            <span className="cv-booking-detail-label">Seats</span>
            <span className="cv-booking-seat-list">
              {seats.length > 0
                ? seats.map(s => <span key={s.id} className="cv-booking-seat">{s.seatNumber}</span>)
                : '—'}
            </span>
          </div>
          <div className="cv-booking-detail-row">
            <span className="cv-booking-detail-label">Status</span>
            <Badge variant={cfg.variant}>{cfg.label}</Badge>
          </div>
        </div>

        <div className="cv-booking-confirm-total">
          <span>Total</span>
          <span>₹{booking.totalPrice?.toLocaleString('en-IN') || '—'}</span>
        </div>

        <div className="cv-booking-confirm-actions">
          {canPay && (
            <Button fullWidth onClick={handlePayment} loading={paymentLoading}>
              <CreditCard size={16} /> Pay Now
            </Button>
          )}
          {canCancel && (
            <Button variant="danger" fullWidth={!canPay} onClick={handleCancel}>
              Cancel Booking
            </Button>
          )}
          <Button variant="secondary" onClick={() => navigate('/')}>
            <Home size={16} /> Home
          </Button>
        </div>

        {ticket && (
          <div className="cv-ticket-card">
            <div className="cv-ticket-main">
              <div className="cv-ticket-heading">
                <span className="cv-ticket-icon"><TicketIcon size={18} /></span>
                <div>
                  <span className="cv-ticket-eyebrow">Digital ticket</span>
                  <strong>Ready for your show</strong>
                </div>
              </div>
              <div className="cv-ticket-show">
                <div>
                  <span className="cv-ticket-label">Booking ID</span>
                  <strong>{ticket.ticketNumber}</strong>
                </div>
                {qrCodeUrl && (
                  <img className="cv-ticket-qr" src={qrCodeUrl} alt="Scannable ticket details QR code" />
                )}
              </div>
              <div className="cv-ticket-meta">
                <span><CalendarDays size={14} /> {show ? new Date(show.showDate).toLocaleDateString('en-IN', { day: 'numeric', month: 'short' }) : '—'}</span>
                <span><MapPin size={14} /> {theatre?.name || '—'}</span>
              </div>
            </div>
            <div className="cv-ticket-stub">
              <span className="cv-ticket-label">Issued</span>
              <strong>{new Date(ticket.generatedAt).toLocaleDateString('en-IN')}</strong>
              <span className="cv-ticket-stub-mark">SCAN QR</span>
            </div>
          </div>
        )}
      </div>
    </div>
  );
}
