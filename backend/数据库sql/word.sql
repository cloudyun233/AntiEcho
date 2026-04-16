create table word
(
    id         int auto_increment
        primary key,
    popularity int          not null,
    word       varchar(255) null
);

