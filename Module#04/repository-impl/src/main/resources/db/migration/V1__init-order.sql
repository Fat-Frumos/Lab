DROP TABLE IF EXISTS orders;

CREATE TABLE orders
(
    order_id       SERIAL PRIMARY KEY,
    order_date     TIMESTAMP,
    user_id        INTEGER REFERENCES users (user_id),
    certificate_id INTEGER REFERENCES gift_certificates (id),
    cost         DECIMAL(10, 2)
);
