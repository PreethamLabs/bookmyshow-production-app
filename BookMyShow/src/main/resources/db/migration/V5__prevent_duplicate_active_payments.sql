CREATE UNIQUE INDEX uk_payment_active_booking
    ON payment (booking_id)
    WHERE payment_status IN ('CREATED', 'PROCESSING');