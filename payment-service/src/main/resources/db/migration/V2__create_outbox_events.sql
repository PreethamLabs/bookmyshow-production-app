CREATE TABLE outbox_events (
                               id BIGSERIAL PRIMARY KEY,
                               event_id VARCHAR(100) NOT NULL UNIQUE,
                               event_type VARCHAR(50) NOT NULL,
                               booking_id BIGINT NOT NULL,
                               payload TEXT NOT NULL,
                               published BOOLEAN NOT NULL DEFAULT FALSE,
                               created_at TIMESTAMP NOT NULL,
                               published_at TIMESTAMP
);