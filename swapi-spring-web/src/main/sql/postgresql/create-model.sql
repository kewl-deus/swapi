create table film
(
    id            varchar(255) not null,
    director      varchar(255),
    episode_id    int4         not null,
    opening_crawl varchar(2000),
    title         varchar(255),
    primary key (id)
)
