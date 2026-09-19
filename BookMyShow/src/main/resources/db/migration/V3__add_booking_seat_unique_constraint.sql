ALTER TABLE booking_seat
    ADD CONSTRAINT uk_booking_seat_show_seat
        UNIQUE (show_seat_id);