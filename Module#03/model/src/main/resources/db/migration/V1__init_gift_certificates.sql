DROP TABLE IF EXISTS gift_certificates;

CREATE TABLE gift_certificates
(
    id               SERIAL PRIMARY KEY,
    name             VARCHAR(155),
    description      TEXT,
    price            DECIMAL(10, 2),
    create_date      TIMESTAMP,
    last_update_date TIMESTAMP,
    duration         INTEGER
);

CREATE SEQUENCE gift_certificates_id_seq START WITH 1 INCREMENT BY 1 NO CYCLE;

ALTER TABLE gift_certificates ALTER COLUMN id SET DEFAULT nextval('gift_certificates_id_seq');

CREATE EXTENSION IF NOT EXISTS "uuid-ossp";