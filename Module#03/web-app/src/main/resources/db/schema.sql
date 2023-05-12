-- DROP TABLE IF EXISTS gift_certificates;

CREATE TABLE IF NOT EXISTS gift_certificates
(
    id               SERIAL PRIMARY KEY,
    name             VARCHAR(55) NOT NULL,
    description      VARCHAR(1024) NOT NULL,
    price            DECIMAL(10, 2),
    create_date      TIMESTAMP,
    last_update_date TIMESTAMP,
    duration         INTEGER
);

-- DROP TABLE IF EXISTS tag;

CREATE TABLE IF NOT EXISTS tag
(
    id SERIAL PRIMARY KEY,
    name VARCHAR(64) NOT NULL
);


-- DROP TABLE IF EXISTS gift_certificate_tag;

CREATE TABLE IF NOT EXISTS gift_certificate_tag
(
    gift_certificate_id BIGINT REFERENCES gift_certificates (id),
    tag_id              BIGINT REFERENCES tag (id),
    PRIMARY KEY (gift_certificate_id, tag_id)
);
