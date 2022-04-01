create table role (
    id serial primary key not null,
    name varchar(50)
);

insert into role (name) values ('user');
insert into role (name) values ('admin');

create table person (
    id serial primary key not null,
    nickname varchar(50),
    role_id int references role(id)
);

create table room (
    id serial primary key not null,
    name varchar(50)
);

insert into room (name) values ('music');

create table message (
        id serial primary key not null,
        description text,
        person_id int references person(id)
);