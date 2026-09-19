CREATE TABLE payments (
                          id BIGSERIAL PRIMARY KEY,

                          booking_id BIGINT NOT NULL UNIQUE,

                          payment_status VARCHAR(30) NOT NULL,

                          amount NUMERIC(12, 2) NOT NULL,

                          paid_at TIMESTAMP,

                          refunded_at TIMESTAMP,

                          razorpay_order_id VARCHAR(100) UNIQUE,

                          razorpay_payment_id VARCHAR(100) UNIQUE,

                          created_at TIMESTAMP,

                          updated_at TIMESTAMP
);