CREATE TABLE IF NOT EXISTS users (
     user_id BIGSERIAL PRIMARY KEY,
     user_name CHARACTER VARYING(255) NOT NULL,
     user_email CHARACTER VARYING(500) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS items (
    item_id BIGSERIAL PRIMARY KEY,
    item_name CHARACTER VARYING(255) NOT NULL,
    item_description CHARACTER VARYING(150) NOT NULL,
    is_available BOOLEAN NOT NULL,
    owner_id BIGINT NOT NULL,
    request_id BIGINT,
    CONSTRAINT fk_items_to_users FOREIGN KEY(owner_id) REFERENCES users(user_id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS comments (
    comment_id BIGSERIAL PRIMARY KEY,
    text CHARACTER VARYING(2000) NOT NULL,
    item_id BIGINT NOT NULL,
    author_id BIGINT NOT NULL,
    CONSTRAINT fk_comments_to_users FOREIGN KEY (author_id) REFERENCES users (user_id) ON DELETE CASCADE,
    CONSTRAINT fk_comments_to_items FOREIGN KEY (item_id) REFERENCES items(item_id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS bookings (
    booking_id BIGSERIAL PRIMARY KEY,
    start_date TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    end_date TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    item_id BIGINT NOT NULL,
    booker_id BIGINT NOT NULL,
    status CHARACTER VARYING (50),
    FOREIGN KEY (item_id) REFERENCES items (item_id) ON DELETE CASCADE,
    FOREIGN KEY (booker_id) REFERENCES users (user_id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS requests (
    request_id BIGSERIAL PRIMARY KEY,
    requestor_id BIGINT NOT NULL,
    description  CHARACTER VARYING (200),
    FOREIGN KEY (requestor_id) REFERENCES users (user_id) ON DELETE CASCADE
);