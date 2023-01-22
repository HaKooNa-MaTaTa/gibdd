create table if not exists car_number
(
    digits      int         not null,
    letters     varchar(10) not null,
    region      varchar(10) not null,
    create_date timestamp   not null default now(),

    PRIMARY KEY (digits, letters, region)
);