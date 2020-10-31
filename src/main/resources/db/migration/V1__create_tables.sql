drop table if exists captcha_codes;
create table captcha_codes (
    id integer not null auto_increment,
    code TINYTEXT not null,
    secret_code TINYTEXT not null,
    time datetime(6) not null, primary key (id)
);

drop table if exists global_settings;
create table global_settings (
    id integer not null auto_increment,
    code varchar(255) not null,
    name varchar(255) not null,
    value varchar(255) not null,
    primary key (id)
);

drop table if exists post_comments;
create table post_comments (
    id integer not null auto_increment,
    parent_id integer,
    text TEXT not null,
    time datetime(6) not null,
    post_id integer not null,
    user_id integer not null,
    primary key (id)
);

drop table if exists post_votes;
create table post_votes (
    id integer not null auto_increment,
    time datetime(6) not null,
    value bit not null,
    post_id integer not null,
    user_id integer not null,
    primary key (id)
);

drop table if exists posts;
create table posts (
    id integer not null auto_increment,
    is_active bit not null,
    moderation_status varchar(255) default 'NEW' not null,
    text TEXT not null,
    time datetime(6) not null,
    title varchar(255) not null,
    view_count integer not null,
    moderator_id integer,
    user_id integer not null,
    primary key (id)
);

drop table if exists tag2post;
create table tag2post (
    id integer not null auto_increment,
    post_id integer not null,
    tag_id integer not null,
    primary key (id)
);

drop table if exists tags;
create table tags (
    id integer not null auto_increment,
    name varchar(255) not null,
    primary key (id)
);

drop table if exists users;
create table users (
    id integer not null auto_increment,
    code varchar(255),
    email varchar(255) not null,
    is_moderator bit not null,
    name varchar(255) not null,
    password varchar(255) not null,
    photo varchar(255),
    reg_time datetime(6) not null,
    primary key (id)
);