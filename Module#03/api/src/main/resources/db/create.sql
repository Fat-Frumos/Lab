CREATE TABLE IF NOT EXISTS users
(
    user_id  SERIAL PRIMARY KEY,
    username VARCHAR(55),
    email    VARCHAR(55)
);

-- DROP TABLE IF EXISTS gift_certificates;

CREATE TABLE IF NOT EXISTS gift_certificates
(
    id               SERIAL PRIMARY KEY,
    name             VARCHAR(55),
    description      TEXT,
    price            DECIMAL(10, 2),
    create_date      TIMESTAMP,
    last_update_date TIMESTAMP,
    duration         INTEGER
);

-- DROP TABLE IF EXISTS tags;

CREATE TABLE IF NOT EXISTS tags
(
    id   SERIAL PRIMARY KEY,
    name VARCHAR(55) NOT NULL
);

-- DROP TABLE IF EXISTS gift_certificate_tags;

CREATE TABLE IF NOT EXISTS gift_certificate_tags
(
    gift_certificate_id BIGINT REFERENCES gift_certificates (id),
    tag_id              BIGINT REFERENCES tags (id),
    PRIMARY KEY (gift_certificate_id, tag_id)
);

DROP TABLE IF EXISTS orders CASCADE;

CREATE TABLE orders
(
    order_id       SERIAL PRIMARY KEY,
    order_date     TIMESTAMP,
    cost           DECIMAL(10, 2)
);

DROP TABLE IF EXISTS gift_certificate_order;

CREATE TABLE gift_certificate_order
(
    user_id BIGINT REFERENCES users (user_id),
    gift_certificate_id BIGINT REFERENCES gift_certificates (id),
    order_id            BIGINT REFERENCES orders (order_id),
    PRIMARY KEY (user_id, gift_certificate_id, order_id)
);

CREATE TABLE IF NOT EXISTS user_order
(
    user_id BIGINT REFERENCES users (user_id),
    order_id            BIGINT REFERENCES orders (order_id),
    PRIMARY KEY (user_id, order_id)
);