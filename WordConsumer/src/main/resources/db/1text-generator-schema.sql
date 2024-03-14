Create table words
(
    id            bigserial primary key,
    word          VARCHAR NOT NULL,
    received_date date    not null
);