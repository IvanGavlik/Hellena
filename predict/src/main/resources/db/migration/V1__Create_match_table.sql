CREATE TABLE match (
    id SERIAL PRIMARY KEY,
    home varchar NOT NULL,
    guest varchar NOT NULL,
    tip_1 real,
    tip_X real,
    tip_2 real,
    tip_1X real,
    tip_X2 real,
    tip_12 real,
    match_day date,
    created date
);
