CREATE TABLE notification_logs (
                                   id BIGSERIAL PRIMARY KEY,
                                   event_id VARCHAR(100) NOT NULL,
                                   channel VARCHAR(30) NOT NULL,
                                   recipient VARCHAR(100) NOT NULL,
                                   message VARCHAR(500) NOT NULL,
                                   status VARCHAR(30) NOT NULL,
                                   created_at TIMESTAMP NOT NULL,
                                   sent_at TIMESTAMP,
                                   failure_reason VARCHAR(500),

                                   CONSTRAINT uk_notification_event_channel
                                       UNIQUE (event_id, channel)
);