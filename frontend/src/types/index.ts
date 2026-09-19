/* ========================================
   Marquee — TypeScript Types
   Mirror of backend DTOs and enums
   ======================================== */

// ---- Enums ----

export enum MovieStatus {
  DRAFT = 'DRAFT',
  UPCOMING = 'UPCOMING',
  RELEASED = 'RELEASED',
  ARCHIVED = 'ARCHIVED',
}

export enum SeatType {
  REGULAR = 'REGULAR',
  PREMIER = 'PREMIER',
  RECLINER = 'RECLINER',
}

export enum SeatStatus {
  AVAILABLE = 'AVAILABLE',
  BOOKED = 'BOOKED',
}

export enum BookingStatus {
  CREATED = 'CREATED',
  PENDING_PAYMENT = 'PENDING_PAYMENT',
  CONFIRMED = 'CONFIRMED',
  FAILED = 'FAILED',
  CANCELLED = 'CANCELLED',
  EXPIRED = 'EXPIRED',
}

export enum AccountStatus {
  NEW = 'NEW',
  ACTIVE = 'ACTIVE',
  BLOCKED = 'BLOCKED',
  SUSPENDED = 'SUSPENDED',
}

export enum MovieRole {
  ACTOR = 'ACTOR',
  DIRECTOR = 'DIRECTOR',
  WRITER = 'WRITER',
  PRODUCER = 'PRODUCER',
}

export enum Role {
  USER = 'USER',
  ADMIN = 'ADMIN',
  THEATRE_OWNER = 'THEATRE_OWNER',
}

// ---- Response DTOs ----

export interface CityResponse {
  id: number;
  name: string;
  state: string;
  country: string;
}

export interface GenreResponse {
  id: number;
  name: string;
}

export interface LanguageResponse {
  id: number;
  name: string;
}

export interface PersonResponse {
  id: number;
  name: string;
}

export interface MovieResponse {
  id: number;
  title: string;
  description: string;
  posterUrl: string;
  movieStatus: MovieStatus;
  durationInMinutes: number;
  releaseDate: string;
}

export interface MovieGenreResponse {
  id: number;
  movieId: number;
  genreId: number;
}

export interface MovieLanguageResponse {
  id: number;
  movieId: number;
  languageId: number;
}

export interface MoviePersonResponse {
  id: number;
  movieId: number;
  personId: number;
  role: MovieRole;
}

export interface TheatreResponse {
  id: number;
  name: string;
  address: string;
  cityId: number;
}

export interface ScreenResponse {
  id: number;
  name: string;
  theatreId: number;
}

export interface SeatResponse {
  id: number;
  seatNumber: string;
  seatType: SeatType;
  screenId: number;
}

export interface ShowResponse {
  id: number;
  movieId: number;
  screenId: number;
  showDate: string;
  ticketPrice: number;
  startTime: string;
  endTime: string;
}

export interface ShowSeatResponse {
  id: number;
  showId: number;
  seatId: number;
  seatNumber: string;
  seatType: SeatType;
  seatStatus: SeatStatus;
  lockedUntil: string | null;
}

export interface UserResponse {
  id: number;
  name: string;
  email: string;
  phoneNumber: string;
  accountStatus: AccountStatus;
  createdAt: string;
  updatedAt: string;
}

export interface BookingResponse {
  id: number;
  userId: number;
  showId: number;
  bookingStatus: BookingStatus;
  totalPrice: number;
  createdAt: string;
  bookedAt: string | null;
  cancelledAt: string | null;
}

export interface BookingSeatResponse {
  id: number;
  bookingId: number;
  showSeatId: number;
  seatNumber: string;
  seatType: SeatType;
  price: number;
}

export interface TicketResponse {
  id: number;
  bookingId: number;
  ticketNumber: string;
  generatedAt: string;
}

export interface PaymentServiceResponse {
  id: number;
  bookingId: number;
  paymentStatus: string;
  amount: number;
  razorpayOrderId: string;
  razorpayPaymentId: string | null;
  createdAt: string;
}

// ---- Request DTOs ----

export interface LoginRequest {
  email: string;
  password: string;
}

export interface UserCreateRequest {
  name: string;
  email: string;
  password: string;
  phoneNumber: string;
}

export interface BookingCreateRequest {
  showId: number;
  showSeatIds: number[];
}

export interface MovieCreateRequest {
  title: string;
  description: string;
  posterUrl: string;
  movieStatus: MovieStatus;
  durationInMinutes: number;
  releaseDate: string;
}

export interface MovieUpdateRequest {
  title: string;
  description: string;
  posterUrl: string;
  movieStatus: MovieStatus;
  durationInMinutes: number;
  releaseDate: string;
}

export interface ShowCreateRequest {
  movieId: number;
  screenId: number;
  showDate: string;
  ticketPrice: number;
  startTime: string;
  endTime: string;
}

export interface ShowUpdateRequest {
  movieId: number;
  screenId: number;
  showDate: string;
  ticketPrice: number;
  startTime: string;
  endTime: string;
}

export interface TheatreCreateRequest {
  name: string;
  address: string;
  cityId: number;
}

export interface TheatreUpdateRequest {
  name: string;
  address: string;
  cityId: number;
}

export interface ScreenCreateRequest {
  name: string;
  theatreId: number;
}

export interface CityCreateRequest {
  name: string;
  state: string;
  country: string;
}

export interface GenreCreateRequest {
  name: string;
}

export interface LanguageCreateRequest {
  name: string;
}

export interface PersonCreateRequest {
  name: string;
}

export interface MovieGenreCreateRequest {
  movieId: number;
  genreId: number;
}

export interface MovieLanguageCreateRequest {
  movieId: number;
  languageId: number;
}

export interface MoviePersonCreateRequest {
  movieId: number;
  personId: number;
  role: MovieRole;
}

// ---- Auth ----

export interface LoginResponse {
  token: string;
}

export interface JwtPayload {
  sub: string;
  role: Role;
  iat: number;
  exp: number;
}

// ---- Enriched frontend-only types ----

export interface MovieDetail extends MovieResponse {
  genres: GenreResponse[];
  languages: LanguageResponse[];
  cast: Array<{ person: PersonResponse; role: MovieRole }>;
}

export interface ShowWithDetails extends ShowResponse {
  theatre: TheatreResponse;
  screen: ScreenResponse;
}

export interface BookingWithDetails extends BookingResponse {
  show?: ShowResponse;
  movie?: MovieResponse;
  theatre?: TheatreResponse;
  screen?: ScreenResponse;
  seats?: BookingSeatResponse[];
  ticket?: TicketResponse;
}
