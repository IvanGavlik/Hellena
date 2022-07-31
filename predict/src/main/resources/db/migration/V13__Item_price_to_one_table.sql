DROP TABLE price;

alter table item
drop description;

alter table item
drop long_name;

alter table item
drop price_id;

alter table item
add price numeric;

alter table item
add active_from date not null;

alter table item
add active_to date not null;

alter table item
add action_price numeric;

