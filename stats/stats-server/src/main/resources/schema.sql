DROP TABLE IF EXISTS endpoints;

CREATE TABLE IF NOT EXISTS endpoints (
id               INT GENERATED     BY DEFAULT AS IDENTITY PRIMARY KEY,
app_name         VARCHAR(40)       NOT NULL,
uri              VARCHAR(100)      NOT NULL,
ip               VARCHAR(16)       NOT NULL,
date_of_request  TIMESTAMP         NOT NULL
);