
CREATE SCHEMA IF NOT EXISTS inventory;
SET SCHEMA inventory;

CREATE TABLE IF NOT EXISTS product(
    id long AUTO_INCREMENT PRIMARY KEY,
    name varchar(255),
    price double,
    quantity int
);

CREATE TABLE IF NOT EXISTS account
(
    id long AUTO_INCREMENT PRIMARY KEY,
    username varchar(50),
    password varchar(50)
);

insert into product (name, price, quantity)
values ('laptop', 999, 12),
       ('desktop', 1299, 7),
       ('iphone', 599, 30);

insert into account (username, password)
values ('admin', 'admin');

