CREATE SEQUENCE resources_seq;

CREATE TABLE IF NOT EXISTS mp3_files (
    id BIGINT PRIMARY KEY DEFAULT nextval('resources_seq'),
    name VARCHAR(255)
);

