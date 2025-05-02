CREATE TYPE user_role AS ENUM ('ROLE_ADMIN', 'ROLE_USER');

CREATE TABLE users
(
    id         SERIAL PRIMARY KEY UNIQUE,
    username   VARCHAR(128) NOT NULL,
    password   VARCHAR(256) NOT NULL,
    role       user_role    NOT NULL DEFAULT 'ROLE_USER',
    is_enabled BOOLEAN      NOT NULL
);

INSERT INTO users
VALUES (1,
        'user@test.com',
        '$2a$10$u63c/1/ccJzQ8YixRr0cPO12xSGjjtCoinlX4aK4CJVBu7NAj9JjK',
        'ROLE_USER',
        true);
