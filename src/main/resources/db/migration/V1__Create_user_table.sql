create table user
(
    id           bigint auto_increment
        primary key,
    account_id   varchar(100)  null,
    name         varchar(100)  null,
    token        varchar(50)   null,
    gmt_create   bigint        null,
    gmt_modified bigint        null,
    bio          varchar(256)  null,
    avatar_url   varchar(1024) null
);
