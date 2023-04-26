DROP TABLE IF EXISTS gift_certificates;

CREATE TABLE IF NOT EXISTS gift_certificates
(
    id               BIGINT PRIMARY KEY,
    name             VARCHAR(55),
    description      TEXT,
    price            DECIMAL(10, 2),
    create_date      TIMESTAMP,
    last_update_date TIMESTAMP,
    duration         INTEGER
);
