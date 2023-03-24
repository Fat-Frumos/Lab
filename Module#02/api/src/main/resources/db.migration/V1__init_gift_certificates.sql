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
INSERT INTO gift_certificates(name, description, price, create_date, last_update_date, duration)
VALUES ('Christmas Gift Certificate', 'This holiday with our gift certificate!', 50.00, NOW(), NOW(), 60);

INSERT INTO gift_certificates(name, description, price, create_date, last_update_date, duration)
VALUES ('Spa Gift Certificate', 'A day of pampering at our luxurious spa!', 100.00, NOW(), NOW(), 120);

INSERT INTO gift_certificates(name, description, price, create_date, last_update_date, duration)
VALUES ('Birthday Gift Certificate', 'Celebrate your special day with a gift certificate!', 75.00, NOW(), NOW(), 90);

INSERT INTO gift_certificates (name, description, price, create_date, last_update_date, duration)
VALUES ('Valentine', 'A gift certificate for Valentine Day', 49.99, NOW(), NOW(), 30);

INSERT INTO gift_certificates (name, description, price, create_date, last_update_date, duration)
VALUES ('Independence', 'A gift certificate for Independence Day', 29.99, NOW(), NOW(), 30);

INSERT INTO gift_certificates (name, description, price, create_date, last_update_date, duration)
VALUES ('Easter', 'A gift certificate for Easter', 39.99, NOW(), NOW(), 30);

