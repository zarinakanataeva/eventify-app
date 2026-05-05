CREATE TABLE users
(
    id                    BIGSERIAL PRIMARY KEY,
    email                 VARCHAR(255) NOT NULL UNIQUE,
    password              VARCHAR(255) NOT NULL,
    role                  VARCHAR      NOT NULL,
    created_at            TIMESTAMP    NOT NULL,
    updated_at            TIMESTAMP    NOT NULL,
    telegram_linking_code VARCHAR(255),
    telegram_chat_id      BIGINT
);

CREATE TABLE events
(
    id            BIGSERIAL PRIMARY KEY,
    title         VARCHAR(255) NOT NULL,
    description   TEXT,
    date_time     TIMESTAMP    NOT NULL,
    total_tickets INT          NOT NULL,
    cover_url     VARCHAR,
    created_at    TIMESTAMP    NOT NULL,
    updated_at    TIMESTAMP    NOT NULL
);

CREATE TABLE bookings
(
    id              BIGSERIAL PRIMARY KEY,
    bookings_amount INT       NOT NULL,
    confirmed       BOOLEAN   NOT NULL DEFAULT FALSE,
    user_id         BIGINT    NOT NULL,
    event_id        BIGINT    NOT NULL,
    created_at      TIMESTAMP NOT NULL,
    updated_at      TIMESTAMP NOT NULL,

    CONSTRAINT fk_bookings_user FOREIGN KEY (user_id)
        REFERENCES users (id),
    CONSTRAINT fk_bookings_event FOREIGN KEY (event_id)
        REFERENCES events (id)
);


CREATE TABLE notifications
(
    user_id             BIGINT PRIMARY KEY,
    notify_new_events   BOOLEAN,
    notify_upcoming     BOOLEAN,
    hours_before_notify INT,

    CONSTRAINT fk_notifications_user FOREIGN KEY (user_id)
        REFERENCES users (id) ON DELETE CASCADE
);
