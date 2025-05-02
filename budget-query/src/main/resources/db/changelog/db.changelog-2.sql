CREATE TABLE incomes
(
    id        VARCHAR PRIMARY KEY UNIQUE,
    amount    DECIMAL(10, 2) NOT NULL CHECK (amount > 0),
    category  VARCHAR(256)   NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    note      TEXT,
    user_id   VARCHAR(256),
    version   BIGINT DEFAULT 0 NOT NULL
);
