DROP TABLE IF EXISTS stats CASCADE;

CREATE TABLE IF NOT EXISTS stats (
    id INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    app VARCHAR(64) NOT NULL,
    uri VARCHAR(64) NOT NULL,
    ip VARCHAR(32) NOT NULL,
    creation_time TIMESTAMP NOT NULL
);