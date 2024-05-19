DROP TABLE IF EXISTS compilation_event CASCADE;
DROP TABLE IF EXISTS compilations CASCADE;
DROP TABLE IF EXISTS events CASCADE;
DROP TABLE IF EXISTS users CASCADE;
DROP TABLE IF EXISTS locations CASCADE;
DROP TABLE IF EXISTS categories CASCADE;
DROP TABLE IF EXISTS requests CASCADE;
DROP TABLE IF EXISTS compilation_events CASCADE;

CREATE TABLE IF NOT EXISTS users (
id               INT GENERATED                    BY DEFAULT AS IDENTITY PRIMARY KEY,
email            VARCHAR                          NOT NULL,
name             VARCHAR                          NOT NULL,
UNIQUE (email)
);

CREATE TABLE IF NOT EXISTS locations (
id               INT GENERATED                    BY DEFAULT AS IDENTITY PRIMARY KEY,
lat              FLOAT,
lon              FLOAT,
UNIQUE (lat, lon)
);

CREATE TABLE IF NOT EXISTS categories (
id               INT GENERATED                    BY DEFAULT AS IDENTITY PRIMARY KEY,
name             VARCHAR       UNIQUE
);

CREATE TABLE IF NOT EXISTS events (
id                   INT GENERATED                BY DEFAULT AS IDENTITY PRIMARY KEY,
annotation           VARCHAR,
category             INT                          REFERENCES categories(id),
confirmed_requests   INT,
created_on           TIMESTAMP,
description          VARCHAR,
event_date           TIMESTAMP,
initiator            INT                          REFERENCES users(id),
location             INT                          REFERENCES locations(id),
paid                 BOOLEAN,
participant_limit    INT,
published_on         TIMESTAMP,
request_moderation   BOOLEAN,
state                VARCHAR,
title                VARCHAR,
views                INT
);

CREATE TABLE IF NOT EXISTS compilations (
id                   INT GENERATED                BY DEFAULT AS IDENTITY PRIMARY KEY,
pinned               BOOLEAN,
title                VARCHAR
);

CREATE TABLE IF NOT EXISTS compilation_events (
compilation_id       INTEGER                      NOT NULL,
event_id             INTEGER                      NOT NULL,
CONSTRAINT compilation_event_pk PRIMARY KEY(compilation_id, event_id),
FOREIGN KEY (compilation_id) REFERENCES compilations (id),
FOREIGN KEY (event_id) REFERENCES events (id)
);

CREATE TABLE IF NOT EXISTS requests (
id                   INT GENERATED                BY DEFAULT AS IDENTITY PRIMARY KEY,
created              TIMESTAMP,
event_id             INT                          REFERENCES events(id),
requester            INT                          REFERENCES users(id),
status               VARCHAR,
UNIQUE(event_id, requester)
 );