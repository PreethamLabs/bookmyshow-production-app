import { api } from './client';
import type {
  BookingCreateRequest,
  BookingResponse,
  BookingSeatResponse,
  TicketResponse,
  PaymentServiceResponse,
} from '../types';

export const bookingApi = {
  create: (data: BookingCreateRequest) =>
    api.post<BookingResponse>('/bookings', data),

  getMyBookings: () =>
    api.get<BookingResponse[]>('/bookings/my'),

  getById: (id: number) =>
    api.get<BookingResponse>(`/bookings/${id}`),

  cancel: (id: number) =>
    api.put<BookingResponse>(`/bookings/${id}/cancel`),

  createPayment: (id: number) =>
    api.post<PaymentServiceResponse>(`/bookings/${id}/payment`),

  verifyPayment: (data: {
    razorpayOrderId: string;
    razorpayPaymentId: string;
    razorpaySignature: string;
  }) => api.post<PaymentServiceResponse>('/bookings/payment/verify', data),

};

export const bookingSeatApi = {
  getByBooking: (bookingId: number) =>
    api.get<BookingSeatResponse[]>(`/booking-seats/booking/${bookingId}`),
};

export const ticketApi = {
  getById: (id: number) =>
    api.get<TicketResponse>(`/tickets/${id}`),

  getByBooking: (bookingId: number) =>
    api.get<TicketResponse>(`/tickets/booking/${bookingId}`),
};
