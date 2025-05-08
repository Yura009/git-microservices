CREATE TABLE IF NOT EXISTS song (
    id BIGINT PRIMARY KEY CHECK (id > 0),
    name VARCHAR(100) NOT NULL,
    artist VARCHAR(100) NOT NULL,
    album VARCHAR(100) NOT NULL,
    duration VARCHAR(5),
    year VARCHAR(4)
);