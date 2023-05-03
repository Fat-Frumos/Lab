-- DROP TABLE IF EXISTS gift_certificate_tags;

CREATE TABLE IF NOT EXISTS gift_certificate_tags
(
    gift_certificate_id BIGINT REFERENCES gift_certificates (id),
    tag_id              BIGINT REFERENCES tag (id),
    PRIMARY KEY (gift_certificate_id, tag_id)
);

DROP TABLE IF EXISTS gift_certificate_order;

CREATE TABLE gift_certificate_order
(
    user_id BIGINT REFERENCES users (user_id),
    gift_certificate_id BIGINT REFERENCES gift_certificates (id),
    order_id            BIGINT REFERENCES orders (order_id),
    PRIMARY KEY (user_id, gift_certificate_id, order_id)
);

CREATE TABLE user_order
(
    user_id BIGINT REFERENCES users (user_id),
    order_id            BIGINT REFERENCES orders (order_id),
    PRIMARY KEY (user_id, order_id)
);