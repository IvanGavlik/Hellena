ALTER TABLE item DROP store_id;

CREATE TABLE item_store (
    item_id integer NOT NULL,
    store_id integer NOT NULL
);
