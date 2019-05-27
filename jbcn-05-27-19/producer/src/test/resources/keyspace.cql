DROP KEYSPACE IF EXISTS space;

CREATE KEYSPACE space WITH replication={'class' : 'SimpleStrategy', 'replication_factor':1};
USE space;

create table stars(
id TIMEUUID,
name varchar,
magnitude double,
primary key(name)
)