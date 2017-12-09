# Queries log table

# --- !Ups
create table QUERIES_LOG (
    ID bigint primary key auto_increment,
    QUERY_TIME timestamp,
    CITY varchar(255)
);

# --- !Downs

drop table QUERIES_LOG if exists;