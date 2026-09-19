CREATE TABLE ticket (
                        id BIGSERIAL PRIMARY KEY,
                        booking_id BIGINT NOT NULL,
                        ticket_number VARCHAR(50) NOT NULL UNIQUE,
                        generated_at TIMESTAMP NOT NULL,

                        CONSTRAINT fk_ticket_booking
                            FOREIGN KEY (booking_id)
                                REFERENCES booking(id),

                        CONSTRAINT uk_ticket_booking
                            UNIQUE (booking_id)
);