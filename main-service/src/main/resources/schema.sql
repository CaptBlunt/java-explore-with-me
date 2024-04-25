DROP TABLE IF EXISTS users;

CREATE TABLE IF NOT EXISTS users (
id               INT GENERATED     BY DEFAULT AS IDENTITY PRIMARY KEY,
first_name       VARCHAR(40)       NOT NULL,
last_name        VARCHAR(40)       NOT NULL
);