CREATE TABLE msg (
    id SERIAL PRIMARY KEY,
    msg_from varchar NOT NULL,
    msg_to varchar NOT NULL,
    header varchar,
    body text NOT NULL
);
