DROP TABLE IF EXISTS gift_certificates;

CREATE TABLE gift_certificates
(
    id               SERIAL PRIMARY KEY,
    name             VARCHAR(55),
    description      TEXT,
    price            DECIMAL(10, 2),
    create_date      TIMESTAMP,
    last_update_date TIMESTAMP,
    duration         INTEGER
);
