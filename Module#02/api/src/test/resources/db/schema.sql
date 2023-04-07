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

DROP TABLE IF EXISTS tag;

CREATE TABLE tag
(
    id   SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL
);


DROP TABLE IF EXISTS gift_certificate_tag;

CREATE TABLE gift_certificate_tag
(
    gift_certificate_id BIGINT REFERENCES gift_certificates (id),
    tag_id              BIGINT REFERENCES tag (id),
    PRIMARY KEY (gift_certificate_id, tag_id)
);
