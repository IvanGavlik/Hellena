CREATE TABLE item_image (
    id SERIAL PRIMARY KEY,
    name varchar NOT NULL,
    content bytea NOT NULL
);

alter table item
    add image_id integer;
