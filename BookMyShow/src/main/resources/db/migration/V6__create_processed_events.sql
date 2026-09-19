CREATE TABLE processed_events (
                                  id BIGSERIAL PRIMARY KEY,
                                  event_id VARCHAR(100) NOT NULL UNIQUE,
                                  event_type VARCHAR(50) NOT NULL,
                                  processed_at TIMESTAMP NOT NULL
);