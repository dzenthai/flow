CREATE TABLE incomes
(
    id        UUID PRIMARY KEY UNIQUE,
    amount    DECIMAL(10, 2) NOT NULL CHECK (amount > 0),
    category  VARCHAR(255)   NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    note      TEXT,
    user_id   VARCHAR(256)
);