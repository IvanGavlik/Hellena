create table category (
    id serial,
    name varchar(50) not null unique,
    description varchar(200) null,
    sub_category integer null
);

create table location (
    id serial not null,
    street varchar(100) not null,
    house_number varchar(10) not null,
    city varchar(50) not null,
    postal_code varchar(10) not null,
    district varchar(50),
    country_code varchar(10) not null
);

create table store (
    id serial,
    name varchar(300) not null,
    company varchar(300) not null,
    location_id integer not null
);

create table price (
    id serial,
    price decimal not null,
    active_from date not null,
    active_to date not null,
    previous integer null
);

create table item (
    id serial,
    name varchar(150) not null,
    long_name varchar(500) not null,
    description varchar(1000) not null,
    price decimal not null,
    category_id integer not null,
    store_id integer not null,
    price_history integer null
);
