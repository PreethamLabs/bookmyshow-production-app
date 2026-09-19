ALTER TABLE payments
DROP CONSTRAINT IF EXISTS payments_booking_id_key;

CREATE UNIQUE INDEX uk_payment_active_booking
    ON payments (booking_id)
    WHERE payment_status IN ('CREATED', 'PROCESSING');