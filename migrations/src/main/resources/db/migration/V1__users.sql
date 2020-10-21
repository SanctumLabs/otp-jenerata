create table if not exists users
(
    id                 int          not null,
    created_on         timestamp default current_timestamp,
    updated_on         timestamp default current_timestamp,
    identifier         varchar(255) not null,
    external_identifier         varchar(255) null,
    first_name       varchar(250)  not null,
    last_name       varchar(250)  not null,
    email_address              varchar(255) not null,
    email_verified              boolean  default false,
    phone_number              varchar(255) not null,
    gender        varchar(50) not null,
    account_status   varchar(15) not null,
    verification_email_sent   boolean default false not null,
    user_type         varchar(20)  not null,
    verify_token         varchar(255)  null,
    verify_expiry_date         timestamp   null,
    constraint user_pk
        primary key (id)
);

create unique index user_identifier_uindex
    on users (identifier);

create unique index user_external_identifier_uindex
    on users (external_identifier);
