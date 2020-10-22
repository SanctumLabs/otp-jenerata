create table if not exists user_otps
(
    id                 int          not null,
    created_on         timestamp default current_timestamp,
    updated_on         timestamp default current_timestamp,
    otp_code         varchar(6) not null,
    phone_number              varchar(255) not null,
    otp_used        boolean default false not null,
    expiry_time       timestamp  not null,
    constraint user_otp_pk
        primary key (id)
);

create unique index user_phone_number_uindex
    on user_otps (phone_number);
