alter table item
drop price;

alter table item
drop price_history;

alter table item
add price_id integer not null;
