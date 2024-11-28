create table car (
id serial primary key,
brand varchar(100) not null,
model varchar(100) not null,
price bigint not null
);

create table driver (
id serial primary key,
name varchar(100) not null,
age smallint not null,
has_driving_license boolean not null,
car_id serial references car(id)
);